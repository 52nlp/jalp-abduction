package uk.co.mtford.jalp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.co.mtford.jalp.abduction.Result;
import uk.co.mtford.jalp.abduction.logic.instance.IInferableInstance;
import uk.co.mtford.jalp.abduction.logic.instance.IUnifiableInstance;
import uk.co.mtford.jalp.abduction.logic.instance.PredicateInstance;
import uk.co.mtford.jalp.abduction.logic.instance.equalities.InEqualityInstance;
import uk.co.mtford.jalp.abduction.logic.instance.term.CharConstantInstance;
import uk.co.mtford.jalp.abduction.logic.instance.term.IntegerConstantInstance;
import uk.co.mtford.jalp.abduction.logic.instance.term.VariableInstance;
import uk.co.mtford.jalp.abduction.parse.program.ParseException;
import uk.co.mtford.jalp.abduction.tools.UniqueIdGenerator;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: mtford
 * Date: 01/06/2012
 * Time: 16:37
 * To change this template use File | Settings | File Templates.
 */
public class AbducibleTest {

    JALPSystem system;

    public AbducibleTest() {

    }

    @Before
    public void noSetup() {

    }

    @After
    public void noTearDown() {

    }

    /*
    boy(john).
    likes(X,Y) :- boy(X), girl(Y).
    abducible(girl(Y)).

    Q = likes(john,Y).

    We expect to collect an abducible girl(Y).
     */
    @Test
    public void abducibleTest1() throws InterruptedException,  ParseException, JALPException, uk.co.mtford.jalp.abduction.parse.query.ParseException, FileNotFoundException {
        UniqueIdGenerator.reset();

        system = new JALPSystem("examples/basic/abducible/abducible.alp");
        List<IInferableInstance> query = new LinkedList<IInferableInstance>();
        VariableInstance Y = new VariableInstance("Y");
        PredicateInstance likes = new PredicateInstance("likes",new CharConstantInstance("john"),Y);
        query.add(likes);
        List<Result> result = system.query(query);
        assertTrue(result.size()==1);
        Result resultOne = result.remove(0);
        assertTrue(resultOne.getStore().abducibles.size()==1);
        assertTrue(resultOne.getStore().abducibles.get(0).isSameFunction(new PredicateInstance("girl",Y)));
    }

    /*
   boy(john).
   likes(X,Y) :- boy(X), girl(Y).
   abducible(girl(Y)).

   Q = likes(john,jane).

   We expect to collect an abducible girl(jane).
    */
    @Test
    public void abducibleTest2() throws InterruptedException,  ParseException, JALPException, uk.co.mtford.jalp.abduction.parse.query.ParseException, FileNotFoundException {
        UniqueIdGenerator.reset();

        system = new JALPSystem("examples/basic/abducible/abducible.alp");
        List<IInferableInstance> query = new LinkedList<IInferableInstance>();
        CharConstantInstance john = new CharConstantInstance(("john"));
        CharConstantInstance jane = new CharConstantInstance("jane");
        PredicateInstance likes = new PredicateInstance("likes",john,jane);
        query.add(likes);
        List<Result> result = system.query(query);
        assertTrue(result.size()==1);
        Result resultOne = result.remove(0);

        assertTrue(resultOne.getStore().abducibles.size()==1);
        assertTrue(resultOne.getStore().abducibles.get(0).equals(new PredicateInstance("girl", jane)));
    }

    /*
    p(X) :- a(X), not q(X).
    q(1).

    abducible(a(X)).

    Q = p(X).

    We expect to collect an inequality X!=1
    */
    @Test
    public void ungroundAbducibleTest1() throws InterruptedException,  ParseException, JALPException, uk.co.mtford.jalp.abduction.parse.query.ParseException, FileNotFoundException {
        UniqueIdGenerator.reset();

        system = new JALPSystem("examples/basic/abducible/unground-abducible.alp");
        List<IInferableInstance> query = new LinkedList<IInferableInstance>();
        VariableInstance X = new VariableInstance("X");
        PredicateInstance a = new PredicateInstance("p",X);
        query.add(a);
        List<Result> result = system.query(query);
        assertTrue(result.size()==1);
        Result resultOne = result.get(0);
        assertTrue(resultOne.getStore().abducibles.size()==1);
        IUnifiableInstance XAssignment = resultOne.getAssignments().get(X);
        assertTrue(resultOne.getStore().abducibles.get(0).equals(new PredicateInstance("a",XAssignment)));
        assertTrue(resultOne.getStore().inequalities.contains(new InEqualityInstance(XAssignment,new IntegerConstantInstance(1))));
    }

    /*
    p(X,Y,Z) :- a(X,Y,Z), not q(X,Y,Z).

    q(1,2,3).

    abducible(a(X,Y,Z)).


    We expect 3 results: X!=1, Y!=2 or Z!=3.
    */
    @Test
    public void ungroundAbducibleTest2() throws InterruptedException,  ParseException, JALPException, uk.co.mtford.jalp.abduction.parse.query.ParseException, FileNotFoundException {
        UniqueIdGenerator.reset();

        system = new JALPSystem("examples/basic/abducible/unground-abducible-2.alp");
        List<IInferableInstance> query = new LinkedList<IInferableInstance>();
        VariableInstance X = new VariableInstance("X");
        VariableInstance Y = new VariableInstance("Y");
        VariableInstance Z = new VariableInstance("Z");
        PredicateInstance a = new PredicateInstance("p",X,Y,Z);
        query.add(a);
        List<Result> result = system.query(query);
        assertTrue(result.size()==3);
        boolean one = false;
        boolean two = false;
        boolean three = false;

        for (Result r:result) {
            IUnifiableInstance XAssignment = r.getAssignments().get(X);
            IUnifiableInstance YAssignment = r.getAssignments().get(Y);
            IUnifiableInstance ZAssignment = r.getAssignments().get(Z);

            assertTrue(r.getStore().abducibles.size()==1);
            if (r.getStore().inequalities.contains(new InEqualityInstance(XAssignment, new IntegerConstantInstance(1)))) {
                if (r.getStore().abducibles.get(0).equals(new PredicateInstance("a",XAssignment,YAssignment,ZAssignment)))
                     {

                             one = true;

                    }
            }
            if (r.getStore().inequalities.contains(new InEqualityInstance(YAssignment,new IntegerConstantInstance(2))))  {
                if (
                    r.getStore().abducibles.get(0).equals(new PredicateInstance("a",new IntegerConstantInstance(1),YAssignment,ZAssignment))) {
                    two = true;

                }
            }

            if (r.getStore().inequalities.contains(new InEqualityInstance(ZAssignment,new IntegerConstantInstance(3)))) {
                if ( r.getStore().abducibles.get(0).equals(new PredicateInstance("a",new IntegerConstantInstance(1),new IntegerConstantInstance(2),ZAssignment))) {
                    three = true;
                }
            }

        }
        assertTrue(one);
        assertTrue(two);
        assertTrue(three);
    }
}
