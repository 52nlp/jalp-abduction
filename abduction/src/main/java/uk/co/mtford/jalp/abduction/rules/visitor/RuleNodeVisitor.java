package uk.co.mtford.jalp.abduction.rules.visitor;

import org.apache.log4j.Logger;
import org.javatuples.Pair;
import uk.co.mtford.jalp.JALPException;
import uk.co.mtford.jalp.abduction.DefinitionException;
import uk.co.mtford.jalp.abduction.Store;
import uk.co.mtford.jalp.abduction.logic.instance.*;
import uk.co.mtford.jalp.abduction.logic.instance.constraints.*;
import uk.co.mtford.jalp.abduction.logic.instance.equalities.*;
import uk.co.mtford.jalp.abduction.logic.instance.term.ListInstance;
import uk.co.mtford.jalp.abduction.logic.instance.term.ConstantInstance;
import uk.co.mtford.jalp.abduction.logic.instance.term.VariableInstance;
import uk.co.mtford.jalp.abduction.rules.*;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: mtford
 * Date: 18/05/2012
 * Time: 06:44
 * To change this template use File | Settings | File Templates.
 */
public class RuleNodeVisitor {

    private static final Logger LOGGER = Logger.getLogger(RuleNodeVisitor.class);

    public RuleNodeVisitor() {

    }

    private RuleNode constructChildNode(List<IInferableInstance> newGoals, RuleNode previousNode) {
        RuleNode newRuleNode;
        if (!(newGoals.isEmpty())) {
            newRuleNode = newGoals.get(0).getPositiveRootRuleNode(previousNode.getAbductiveFramework(), new LinkedList<IInferableInstance>(previousNode.getQuery()), newGoals);
            newRuleNode.setStore(previousNode.getStore().shallowClone());
            newRuleNode.setAssignments(new HashMap<VariableInstance, IUnifiableAtomInstance>(previousNode.getAssignments()));
        }
        else {
            newRuleNode = new LeafRuleNode(previousNode.getAbductiveFramework(),new LinkedList<IInferableInstance>(previousNode.getQuery()),previousNode.getStore().shallowClone(),new HashMap<VariableInstance, IUnifiableAtomInstance>(previousNode.getAssignments()),previousNode);
        }

        return newRuleNode;
    }

    public void visit(A1RuleNode ruleNode) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Applying A1 to node.");

        LinkedList<IInferableInstance> newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());

        // 1st Branch: Unify with an already collected abducible.
        LinkedList<RuleNode> childNodes = new LinkedList<RuleNode>();
        Store store = ruleNode.getStore();
        PredicateInstance goalAbducible = (PredicateInstance) newGoals.remove(0);
        LinkedList<RuleNode> firstBranchChildNodes = getA1FirstBranch(ruleNode, store, goalAbducible, newGoals);
        childNodes.addAll(0,firstBranchChildNodes);

        // Second branch: Add a new abducible. Check satisfies collected nestedDenialsList. Check not possible to unifyLeftRight with any existing.
        RuleNode secondBranchChildNode = getA1SecondBranch(ruleNode, store, goalAbducible, newGoals);
        childNodes.add(secondBranchChildNode);
        if (LOGGER.isInfoEnabled()) LOGGER.info("A1 generated "+childNodes.size()+" new states.");
        expandNode(ruleNode, childNodes);
    }

    private LinkedList<RuleNode> getA1FirstBranch(A1RuleNode ruleNode, Store store, PredicateInstance goalAbducible, List<IInferableInstance> goals) {
        LinkedList<RuleNode> childNodes = new LinkedList<RuleNode>();
        for (PredicateInstance storeAbducible : store.abducibles) {
            if (goalAbducible.isSameFunction(storeAbducible)) {
                List<IInferableInstance> equalitySolved = new LinkedList<IInferableInstance>(storeAbducible.reduce(goalAbducible));
                List<IInferableInstance> newRestOfGoals = new LinkedList<IInferableInstance>(goals);
                newRestOfGoals.addAll(0,equalitySolved);
                RuleNode childNode = constructChildNode(newRestOfGoals, ruleNode);
                childNodes.add(childNode);
            }
        }
        return childNodes;
    }

    private RuleNode getA1SecondBranch(A1RuleNode ruleNode, Store store, PredicateInstance goalAbducible,List<IInferableInstance> goals) {
        // Set up new child node and it's data structures.
        RuleNode childNode;
        List<IInferableInstance> newRestOfGoals = new LinkedList<IInferableInstance>(goals);

        // Check our new collected abducible doesn't violate any collected constraints.
        for (DenialInstance collectedDenial : store.denials) {
            PredicateInstance collectedDenialHead = (PredicateInstance) collectedDenial.getBody().get(0);
            if (collectedDenialHead.isSameFunction(goalAbducible)) {
                DenialInstance newDenial = (DenialInstance) collectedDenial.deepClone(new HashMap<VariableInstance, IUnifiableAtomInstance>(ruleNode.getAssignments()));
                collectedDenialHead = (PredicateInstance) newDenial.getBody().remove(0);
                newDenial.getBody().addAll(0,goalAbducible.reduce(collectedDenialHead));
                newRestOfGoals.add(0, newDenial);
            }
        }

        // Check our new collected abducible won't unifyLeftRight with any already collected abducibles.
        for (PredicateInstance storeAbducible : store.abducibles) {
            if (goalAbducible.isSameFunction(storeAbducible)) {
                DenialInstance denialInstance = new DenialInstance();
                List<EqualityInstance> equalitySolved = goalAbducible.reduce(storeAbducible);
                denialInstance.getBody().addAll(equalitySolved);
                newRestOfGoals.add(0,denialInstance);
            }
        }

        childNode = constructChildNode(newRestOfGoals, ruleNode);
        childNode.getStore().abducibles.add(goalAbducible);

        return childNode;
    }

    public void visit(A2RuleNode ruleNode) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Applying A2 to node.");

        // Set up new child nodes data structures.
        RuleNode childNode;
        List<IInferableInstance> newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        DenialInstance currentGoal = (DenialInstance) newGoals.remove(0).shallowClone();
        PredicateInstance abducibleDenialHead = (PredicateInstance) currentGoal.getBody().remove(0);

        // Check that the new constraint we are creating isn't instantly violated by an already collected abducible.
        Store store = ruleNode.getStore();
        List<DenialInstance> newDenials = new LinkedList<DenialInstance>();
        for (PredicateInstance storeAbducible : store.abducibles) {
            if (storeAbducible.isSameFunction(abducibleDenialHead)) {
                HashMap<VariableInstance, IUnifiableAtomInstance> subst = new HashMap<VariableInstance, IUnifiableAtomInstance>(ruleNode.getAssignments());
                DenialInstance newDenial = (DenialInstance) currentGoal.shallowClone();
                List<EqualityInstance> equalitySolved = storeAbducible.reduce(abducibleDenialHead);
                newDenial.getBody().addAll(0,equalitySolved);
                newDenials.add(newDenial);
            }
        }

        newGoals.addAll(0,newDenials);
        childNode = constructChildNode(newGoals,ruleNode);

        currentGoal.getBody().add(0,abducibleDenialHead);
        childNode.getStore().denials.add(currentGoal);

        expandNode(ruleNode,childNode);
    }

    public void visit(D1RuleNode ruleNode) throws DefinitionException {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Applying D1 to node.");
        List<IInferableInstance> newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        PredicateInstance definedPredicate = (PredicateInstance) newGoals.remove(0);
        List<List<IInferableInstance>> possibleUnfolds = ruleNode.getAbductiveFramework().unfoldDefinitions(definedPredicate);
        LinkedList<RuleNode> childNodes = new LinkedList<RuleNode>();
        for (List<IInferableInstance> possibleUnfold : possibleUnfolds) {
            List<IInferableInstance> goals = new LinkedList<IInferableInstance>(newGoals);
            goals.addAll(0, possibleUnfold);
            RuleNode childNode = constructChildNode(goals, ruleNode);
            childNodes.add(childNode);
        }
        if (LOGGER.isInfoEnabled()) LOGGER.info("D1 generated "+childNodes.size()+" new states.");
        expandNode(ruleNode,childNodes);
    }

    public void visit(D2RuleNode ruleNode) throws DefinitionException {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Applying D2 to node.");

        List<IInferableInstance> newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        DenialInstance currentGoal = (DenialInstance) newGoals.remove(0).shallowClone();
        PredicateInstance definedPredicateDenialHead = (PredicateInstance) currentGoal.getBody().remove(0);

        IInferableInstance newGoal = null;

        RuleNode childNode;

        List<List<IInferableInstance>> possibleUnfolds = ruleNode.getAbductiveFramework().unfoldDefinitions(definedPredicateDenialHead);

        List<DenialInstance> newUnfoldedDenials = new LinkedList<DenialInstance>();

        for (List<IInferableInstance> possibleUnfold:possibleUnfolds) {

            HashMap<VariableInstance,IUnifiableAtomInstance> subst = new HashMap<VariableInstance, IUnifiableAtomInstance>(ruleNode.getAssignments());
            DenialInstance newUnfoldedDenial = (DenialInstance) currentGoal.shallowClone();
            Set<VariableInstance> newUniversalVariables = new HashSet<VariableInstance>();
            List<IInferableInstance> toAddToBody = new LinkedList<IInferableInstance>();
            for (IInferableInstance unfold:possibleUnfold) {
                toAddToBody.add((IInferableInstance) unfold.performSubstitutions(subst));
                if (!(unfold instanceof EqualityInstance)) {
                    newUniversalVariables.addAll(unfold.getVariables());
                }
            }
            newUnfoldedDenial.getBody().addAll(0,toAddToBody);
            newUnfoldedDenial.getUniversalVariables().addAll(newUniversalVariables);
            newUnfoldedDenials.add(newUnfoldedDenial);
        }

        newGoals.addAll(0,newUnfoldedDenials);
        childNode = constructChildNode(newGoals,ruleNode);

        expandNode(ruleNode,childNode);

    }

    public void visit(E1RuleNode ruleNode) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Applying E1 to node.");
        RuleNode childNode;
        List<IInferableInstance> newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        EqualityInstance currentGoal = (EqualityInstance) newGoals.remove(0);

        childNode = constructChildNode(newGoals, ruleNode);
        childNode.getStore().equalities.add(currentGoal);

        expandNode(ruleNode,childNode);

    }

    public void visit(InE1RuleNode ruleNode) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Applying E1 Inequality to node.");
        RuleNode childNode;
        List<IInferableInstance> newRestOfGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        InEqualityInstance currentGoal = (InEqualityInstance) newRestOfGoals.remove(0);

        childNode = constructChildNode(newRestOfGoals, ruleNode);
        childNode.getStore().inequalities.add(currentGoal);
        expandNode(ruleNode,childNode);
    }

    public void visit(E2RuleNode ruleNode) {
        List<IInferableInstance> newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        DenialInstance currentGoal = (DenialInstance) newGoals.remove(0).shallowClone();
        EqualityInstance equalityDenialHead = (EqualityInstance) currentGoal.getBody().remove(0);

        IInferableInstance newGoal = null;

        RuleNode childNode;
        List<RuleNode> newChildNodes = new LinkedList<RuleNode>();

        List<EqualityInstance> reductionResult = reductionResult = equalityDenialHead.reduceLeftRight();

        while (!reductionResult.isEmpty()) {
            currentGoal.getBody().addAll(0,reductionResult);
            equalityDenialHead = (EqualityInstance) currentGoal.getBody().remove(0);
        }

        if (equalityDenialHead.getLeft() instanceof ConstantInstance) {  // c=c or for all X c=X
            if (equalityDenialHead.getRight() instanceof VariableInstance) {
                if (!currentGoal.getUniversalVariables().contains(equalityDenialHead.getRight())) {
                    throw new JALPException("WE have a '<- c = Z,Q' as goal where Z is existentially quantified? How to handle this? Should this even occur?rule node:\n" +
                            ruleNode );   // Sanity check.
                }
            }
            HashMap<VariableInstance,IUnifiableAtomInstance> newAssignments = new HashMap<VariableInstance, IUnifiableAtomInstance>(ruleNode.getAssignments());
            boolean unificationSuccess = equalityDenialHead.unifyLeftRight(newAssignments); // Blank assignments as should be just constants.
            if (unificationSuccess) {
                currentGoal.performSubstitutions(newAssignments);
                newGoals.add(0,currentGoal);
                currentGoal.getBody().add(0, new TrueInstance());
            }
            else {
                newGoals.add(0,currentGoal);
                currentGoal.getBody().add(0, new FalseInstance());
            }
            childNode = constructChildNode(newGoals,ruleNode);
            newAssignments.putAll(ruleNode.getAssignments());
            childNode.setAssignments(newAssignments);
            newChildNodes.add(childNode);

        }

        else if (equalityDenialHead.getLeft() instanceof VariableInstance) {
            VariableInstance left = (VariableInstance) equalityDenialHead.getLeft();
            if (currentGoal.getUniversalVariables().contains(left)) {
                if (equalityDenialHead.getRight() instanceof VariableInstance) {
                    if (!currentGoal.getUniversalVariables().contains(equalityDenialHead.getRight())) {
                        throw new JALPException("WE have a '<- Y = Z,Q' as goal where Z is existentially quantified? How to handle this? Should this even occur?rule node:\n" +
                                ruleNode );   // Sanity check.
                    }
                }

                HashMap<VariableInstance,IUnifiableAtomInstance> newAssignments = new HashMap<VariableInstance,IUnifiableAtomInstance>(ruleNode.getAssignments());
                boolean unificationSuccess = equalityDenialHead.unifyLeftRight(newAssignments);

                currentGoal.performSubstitutions(newAssignments);

                if (unificationSuccess) {
                    newGoal = new TrueInstance();
                }
                else {
                    newGoal = new FalseInstance();

                }

                currentGoal.getBody().add(0,newGoal);
                newGoals.add(0,currentGoal);

                childNode = constructChildNode(newGoals,ruleNode);

                newChildNodes.add(childNode);
            }

            else { // Now in equational solved form.
                if (equalityDenialHead.getRight() instanceof VariableInstance) { // E2c
                    if (LOGGER.isInfoEnabled()) LOGGER.info("Applying E2c to node.");
                    HashMap<VariableInstance,IUnifiableAtomInstance> newAssignments = new HashMap<VariableInstance,IUnifiableAtomInstance>(ruleNode.getAssignments());
                    boolean unificationSuccess = equalityDenialHead.unifyRightLeft(newAssignments);
                    if (!unificationSuccess) {
                        throw new JALPException("Error in JALP. E2c failed unification on rule node:\n"+ruleNode);  // Sanity check.
                    }
                    else  {
                        currentGoal = currentGoal.shallowClone();
                        currentGoal = (DenialInstance)currentGoal.performSubstitutions(newAssignments);
                        newGoals.add(0,currentGoal);
                        childNode = constructChildNode(newGoals,ruleNode);
                    }

                    newChildNodes.add(childNode);

                }
                else { // E2b
                    if (LOGGER.isInfoEnabled()) LOGGER.info("Applying E2b to node.");
                    // Branch 1
                    InEqualityInstance inEqualityInstance = new InEqualityInstance(equalityDenialHead);
                    childNode = constructChildNode(newGoals,ruleNode);
                    childNode.getStore().inequalities.add(inEqualityInstance);
                    newChildNodes.add(childNode);
                    // Branch 2
                    newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
                    currentGoal = (DenialInstance) newGoals.remove(0).shallowClone();
                    equalityDenialHead = (EqualityInstance) currentGoal.getBody().remove(0);
                    HashMap<VariableInstance,IUnifiableAtomInstance> newAssignments = new HashMap<VariableInstance,IUnifiableAtomInstance>(ruleNode.getAssignments());
                    boolean unificationSuccess = equalityDenialHead.unifyLeftRight(newAssignments);
                    if (unificationSuccess) {
                        currentGoal = (DenialInstance)currentGoal.performSubstitutions(newAssignments);
                        newGoals.add(0,currentGoal);
                        childNode = constructChildNode(newGoals,ruleNode);
                        childNode.setAssignments(newAssignments);
                        newChildNodes.add(childNode);
                    }
                    else {
                        currentGoal.getBody().add(0, new FalseInstance());
                        newGoals.add(0,currentGoal);
                        childNode = constructChildNode(newGoals,ruleNode);
                        newChildNodes.add(childNode);
                        //throw new JALPException("Equality solver failed in E2b. Should this happen? RuleNode:\n"+ruleNode);
                    }
                }
            }
        }

        expandNode(ruleNode,newChildNodes);
    }

    public void visit(InE2RuleNode ruleNode) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Applying E2 inequality to node.");


        List<IInferableInstance> newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        DenialInstance currentGoal = (DenialInstance) newGoals.remove(0).shallowClone();
        InEqualityInstance inEqualityDenialHead = (InEqualityInstance) currentGoal.getBody().remove(0);

        IInferableInstance newGoal = null;
        RuleNode childNode;
        List<RuleNode> newChildNodes = new LinkedList<RuleNode>();

        // Branch 1
        newGoals.add(0,inEqualityDenialHead.getEqualityInstance());
        childNode = constructChildNode(newGoals,ruleNode);
        newChildNodes.add(childNode);
        // Branch 2
        newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        currentGoal = (DenialInstance) newGoals.remove(0).shallowClone();
        inEqualityDenialHead = (InEqualityInstance) currentGoal.getBody().remove(0);

        newGoals.add(0,currentGoal);
        newGoals.add(inEqualityDenialHead);
        childNode = constructChildNode(newGoals,ruleNode);

        newChildNodes.add(childNode);
        expandNode(ruleNode,newChildNodes);

    }

    public void visit(N1RuleNode ruleNode) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Applying N1 to node.");

        List<IInferableInstance> newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        NegationInstance goal = (NegationInstance) newGoals.remove(0);

        DenialInstance denial = new DenialInstance(goal.getSubFormula());
        newGoals.add(denial);

        RuleNode childNode = constructChildNode(newGoals, ruleNode);
        expandNode(ruleNode,childNode);
    }

    public void visit(N2RuleNode ruleNode) throws JALPException {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Applying N2 to node.");
        // Branch 1:
        List<IInferableInstance> newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        DenialInstance currentGoal = (DenialInstance) newGoals.remove(0).shallowClone();
        NegationInstance negationDenialHead = (NegationInstance) currentGoal.getBody().remove(0);

        RuleNode childNode;
        LinkedList<RuleNode> childNodes = new LinkedList<RuleNode>();

        for (VariableInstance v:negationDenialHead.getVariables()) { // Sanity check.
            if (currentGoal.getUniversalVariables().contains(v)) {
                throw new JALPException("Variables in the negation are universal variables at rulenode:\n"+ruleNode);
            }
        }

        newGoals.add(0,negationDenialHead.getSubFormula());
        childNode = constructChildNode(newGoals,ruleNode);
        childNodes.add(childNode);
        // OR

        newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        currentGoal = (DenialInstance) newGoals.remove(0).shallowClone();
        negationDenialHead = (NegationInstance) currentGoal.getBody().remove(0);

        newGoals.add(0, currentGoal);
        newGoals.add(0, negationDenialHead);
        childNode = constructChildNode(newGoals,ruleNode);

        childNodes.add(childNode);
        expandNode(ruleNode,childNodes);

    }

    public void visit(F1RuleNode ruleNode) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Applying F1 to node.");
        RuleNode childNode;

        List<IInferableInstance> newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        ConstraintInstance currentGoal = (ConstraintInstance) newGoals.remove(0);

        childNode = constructChildNode(newGoals, ruleNode);
        childNode.getStore().constraints.add(currentGoal);
        expandNode(ruleNode,childNode);

    }

    public void visit(F2RuleNode ruleNode) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Applying F2 to node.");

        List<IInferableInstance> newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        DenialInstance currentGoal = (DenialInstance) newGoals.remove(0).shallowClone();
        ConstraintInstance constraintDenialHead = (ConstraintInstance) currentGoal.getBody().remove(0);

        RuleNode childNode;
        List<RuleNode> newChildNodes = new LinkedList<RuleNode>();
        IInferableInstance newGoal = null;

        // Branch 1
        NegativeConstraintInstance negativeConstraintInstance = new NegativeConstraintInstance(constraintDenialHead);
        childNode = constructChildNode(newGoals, ruleNode); // TODO: Rule doesn't say anything about new goals in the child node?
        childNode.getStore().constraints.add(negativeConstraintInstance);
        newChildNodes.add(childNode);
        // Branch 2
        newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        currentGoal = (DenialInstance) newGoals.remove(0).shallowClone();
        constraintDenialHead = (ConstraintInstance) currentGoal.getBody().remove(0);

        newGoals.add(0,currentGoal);

        childNode = constructChildNode(newGoals,ruleNode);
        childNode.getStore().constraints.add(constraintDenialHead);
        newChildNodes.add(childNode);
        expandNode(ruleNode,newChildNodes);

    }

    public void visit(F2bRuleNode ruleNode) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Applying F2b to node.");

        List<IInferableInstance> newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        DenialInstance currentGoal = (DenialInstance) newGoals.remove(0).shallowClone();
        InListConstraintInstance constraintDenialHead = (InListConstraintInstance) currentGoal.getBody().get(0);

        IInferableInstance newGoal;
        RuleNode childNode;

        VariableInstance variable = (VariableInstance) constraintDenialHead.getLeft();
        LinkedList<ConstantInstance> constantList = ((ListInstance)constraintDenialHead.getRight()).getList();

        LinkedList<DenialInstance> newDenials = new LinkedList<DenialInstance>();

        for (ConstantInstance constant:constantList) {
            Map<VariableInstance,IUnifiableAtomInstance> newAssignments = new HashMap<VariableInstance, IUnifiableAtomInstance>(ruleNode.getAssignments());
            boolean unificationSuccess = variable.unify(constant,newAssignments);
            if (unificationSuccess) {
                DenialInstance newDenialInstance = (DenialInstance) currentGoal.shallowClone();
                newDenialInstance = (DenialInstance) newDenialInstance.performSubstitutions(newAssignments);
                newDenials.add(newDenialInstance);
            }
        }

        newGoals.addAll(0,newDenials);
        childNode = constructChildNode(newGoals,ruleNode);

        expandNode(ruleNode,childNode);

    }


        /**
        * Produces one child node where the true instance is removed from the goal stack.
        *
        * @param ruleNode
        */
    public void visit(PositiveTrueRuleNode ruleNode)
    {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Applying truth conjunction rule to node.");

        List<IInferableInstance> newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        newGoals.remove(0); // Remove the truth instance.

        RuleNode newRuleNode = constructChildNode(newGoals, ruleNode);
        expandNode(ruleNode,newRuleNode);

    }

    /**
     * Produces one child node where the true instance is removed from the denial.
     *
     * @param ruleNode
     */
    public void visit(NegativeTrueRuleNode ruleNode) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Applying truth denial conjunction rule to node.");

        List<IInferableInstance> newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());

        DenialInstance currentGoal = (DenialInstance) newGoals.remove(0).shallowClone();
        currentGoal.getBody().remove(0); // Remove true instance.

        newGoals.add(0,currentGoal); // TODO why remove it in the first place?

        RuleNode childNode = constructChildNode(newGoals,ruleNode);

        expandNode(ruleNode,childNode);
    }

    /**
     * Fails the node.
     *
     * @param ruleNode
     */
    public void visit(PositiveFalseRuleNode ruleNode) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Applying false conjunction rule to node.");


        ruleNode.getGoals().remove(0);
        ruleNode.setNodeMark(RuleNode.NodeMark.FAILED);
    }

    /**
     * Produces one child node whereby the nestedDenialsList succeeds.
     *
     * @param ruleNode
     */
    public void visit(NegativeFalseRuleNode ruleNode) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Applying false denial conjunction rule to node.");

        List<IInferableInstance> newGoals = new LinkedList<IInferableInstance>(ruleNode.getGoals());
        newGoals.remove(0); // Remove succeeded denial.
        newGoals.add(0, new TrueInstance());
        RuleNode childNode = constructChildNode(newGoals,ruleNode);
        expandNode(ruleNode,childNode);

    }

    public void visit(LeafRuleNode ruleNode) {
        throw new UnsupportedOperationException();
    }

    private void expandNode(RuleNode parent, RuleNode child) {
        List<RuleNode> newChildren = new LinkedList<RuleNode>();
        if (applyEqualitySolver(child)) {
            List<RuleNode> newConstraintChildren = applyInEqualitySolver(child);
            if (newConstraintChildren==null) {
                child.setNodeMark(RuleNode.NodeMark.FAILED);
                newChildren.add(child);
            }
            else {
                newChildren.addAll(newConstraintChildren);
            }
        }
        else {
            child.setNodeMark(RuleNode.NodeMark.FAILED);
            newChildren.add(child);
        }
        parent.getChildren().addAll(newChildren);
        parent.setNodeMark(RuleNode.NodeMark.EXPANDED);
    }

    private void expandNode(RuleNode parent, List<RuleNode> children) {
        for (RuleNode child:children) {
            expandNode(parent, child);
        }
    }

    private boolean applyEqualitySolver(RuleNode node) {
        Map<VariableInstance,IUnifiableAtomInstance> assignments = new HashMap<VariableInstance, IUnifiableAtomInstance>(node.getAssignments());
        List<EqualityInstance> equalities = new LinkedList<EqualityInstance>(node.getStore().equalities);

        if (!equalities.isEmpty()) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Applying equality solver to rule node:\n"+node);

            IEqualitySolver solver = new EqualitySolver();
            boolean equalitySolveSuccess = solver.execute(assignments,equalities);

            if (equalitySolveSuccess) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug("Equality solver succeeded.");
                node.setAssignments(assignments);
                node.getStore().equalities=new LinkedList<EqualityInstance>();
                return true;
            }

            else {
                if (LOGGER.isDebugEnabled()) LOGGER.debug("Equality solver failed");
                node.setNodeMark(RuleNode.NodeMark.FAILED);
                return false;
            }
        }

        return true;

    }

    private List<RuleNode> applyInEqualitySolver(RuleNode node) throws JALPException {
        List<InEqualityInstance> inequalities = node.getStore().inequalities;
        LinkedList<RuleNode> ruleNodes = new LinkedList<RuleNode>();

        if (!inequalities.isEmpty()) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Applying inequality solver to rulenode:\n"+node);
            for (InEqualityInstance e:inequalities){
                e.performSubstitutions(node.getAssignments()); // TODO: Do this somewhere else i.e. when applying to whole state.
            }
            InEqualitySolver solver = new InEqualitySolver();

            List<Pair<List<EqualityInstance>,List<InEqualityInstance>>> possibleEDECombinations = new LinkedList<Pair<List<EqualityInstance>,List<InEqualityInstance>>>();

            for (InEqualityInstance inEquality:inequalities) {
                List<Pair<List<EqualityInstance>,List<InEqualityInstance>>> inequalitySolved = solver.execute(inEquality);
                if (inequalitySolved==null) {
                    if (LOGGER.isDebugEnabled()) LOGGER.debug("Inequality solver failed to find any solutions");
                    return null;
                }
                if (possibleEDECombinations.isEmpty()) {
                    possibleEDECombinations.addAll(inequalitySolved);
                }
                else {
                    List<Pair<List<EqualityInstance>,List<InEqualityInstance>>> newPossibleEDECombinations = new LinkedList<Pair<List<EqualityInstance>,List<InEqualityInstance>>>();
                    for (Pair<List<EqualityInstance>, List<InEqualityInstance>> newPair:inequalitySolved) {
                         for (Pair<List<EqualityInstance>, List<InEqualityInstance>> oldPair:possibleEDECombinations) {
                             List<EqualityInstance> newEqualities = new LinkedList<EqualityInstance>();
                             List<InEqualityInstance> newInEqualities = new LinkedList<InEqualityInstance>();
                             newEqualities.addAll(newPair.getValue0());
                             newEqualities.addAll(newPair.getValue0());
                             newInEqualities.addAll(newPair.getValue1());
                             newInEqualities.addAll(newPair.getValue1());
                             newPossibleEDECombinations.add(new Pair<List<EqualityInstance>, List<InEqualityInstance>>(newEqualities,newInEqualities));
                         }
                    }
                    possibleEDECombinations = newPossibleEDECombinations;
                }
            }

            if (possibleEDECombinations.isEmpty()) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug("Inequality solver found one solution");
                node.getStore().inequalities=new LinkedList<InEqualityInstance>();
                node.getStore().equalities=new LinkedList<EqualityInstance>();
                ruleNodes.add(node);
            }

            else {
                if (LOGGER.isDebugEnabled()) LOGGER.debug("Inequality solver found "+possibleEDECombinations.size()+" solutions.");
                for (Pair<List<EqualityInstance>, List<InEqualityInstance>> combinations:possibleEDECombinations) {
                    RuleNode newNode = node.shallowClone();
                    newNode.getStore().equalities=combinations.getValue0();
                    newNode.getStore().inequalities=combinations.getValue1();
                    ruleNodes.add(newNode);
                }
            }

        }

        else {
            ruleNodes.add(node);
        }
        return ruleNodes;
    }

}
