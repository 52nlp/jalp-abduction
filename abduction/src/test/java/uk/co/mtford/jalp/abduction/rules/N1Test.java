package uk.co.mtford.jalp.abduction.rules;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.mtford.jalp.JALP;
import uk.co.mtford.jalp.abduction.logic.instance.IInferableInstance;
import uk.co.mtford.jalp.abduction.logic.instance.NegationInstance;
import uk.co.mtford.jalp.abduction.logic.instance.PredicateInstance;
import uk.co.mtford.jalp.abduction.logic.instance.equalities.InEqualityInstance;
import uk.co.mtford.jalp.abduction.logic.instance.term.VariableInstance;
import uk.co.mtford.jalp.abduction.parse.query.JALPQueryParser;
import uk.co.mtford.jalp.abduction.parse.query.ParseException;
import uk.co.mtford.jalp.abduction.tools.UniqueIdGenerator;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mtford
 * Date: 07/06/2012
 * Time: 17:20
 * To change this template use File | Settings | File Templates.
 */
public class N1Test {
    public N1Test() {

    }

    @Before
    public void noSetup() {

    }

    @After
    public void noTearDown() {

    }

    @Test
    public void test1() throws Exception {
        UniqueIdGenerator.reset();

        N1RuleNode ruleNode = new N1RuleNode();
        LinkedList<IInferableInstance> goals = new LinkedList<IInferableInstance>();

        VariableInstance X = new VariableInstance("X");
        VariableInstance Y = new VariableInstance("Y");
        VariableInstance Z = new VariableInstance("Z");

        PredicateInstance p = new PredicateInstance("P",X,Y,Z);

        NegationInstance n = new NegationInstance(p);

        goals.add(n);
        ruleNode.setGoals(goals);

        JALP.applyRule(ruleNode);
        JALP.getVisualizer("debug/rules/N1/Test1",ruleNode);
    }
}
