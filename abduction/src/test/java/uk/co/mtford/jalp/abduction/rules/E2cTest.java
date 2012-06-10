package uk.co.mtford.jalp.abduction.rules;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.mtford.jalp.JALP;
import uk.co.mtford.jalp.abduction.logic.instance.DenialInstance;
import uk.co.mtford.jalp.abduction.logic.instance.IInferableInstance;
import uk.co.mtford.jalp.abduction.logic.instance.PredicateInstance;
import uk.co.mtford.jalp.abduction.logic.instance.equalities.EqualityInstance;
import uk.co.mtford.jalp.abduction.logic.instance.term.VariableInstance;
import uk.co.mtford.jalp.abduction.parse.query.JALPQueryParser;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mtford
 * Date: 07/06/2012
 * Time: 17:20
 * To change this template use File | Settings | File Templates.
 */
public class E2cTest {
    public E2cTest() {

    }

    @Before
    public void noSetup() {

    }

    @After
    public void noTearDown() {

    }

    @Test
    public void test1() throws Exception {
        E2RuleNode ruleNode = new E2RuleNode();
        List<IInferableInstance> goals = JALPQueryParser.readFromString("X=Y, q(Y)");

        VariableInstance X = new VariableInstance("X");
        VariableInstance Y = new VariableInstance("Y");
        EqualityInstance e = new EqualityInstance(X,Y);
        PredicateInstance q = new PredicateInstance("q",Y);
        goals.add(e);
        goals.add(q);

        DenialInstance d = new DenialInstance(goals);
        d.getUniversalVariables().add(Y);
        ruleNode.getGoals().add(d);
        ruleNode.setQuery(new LinkedList<IInferableInstance>(ruleNode.getGoals()));


        JALP.applyRule(ruleNode);
        JALP.getVisualizer("debug/rules/E2c/Test1",ruleNode);
    }

}
