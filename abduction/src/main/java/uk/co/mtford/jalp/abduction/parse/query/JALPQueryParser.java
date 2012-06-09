/* Generated By:JavaCC: Do not edit this line. JALPQueryParser.java */
package uk.co.mtford.jalp.abduction.parse.query;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import uk.co.mtford.jalp.abduction.AbductiveFramework;
import uk.co.mtford.jalp.abduction.logic.instance.IInferableInstance;
import uk.co.mtford.jalp.abduction.logic.instance.DenialInstance;
import uk.co.mtford.jalp.abduction.logic.instance.constraints.*;
import uk.co.mtford.jalp.abduction.logic.instance.equalities.EqualityInstance;
import uk.co.mtford.jalp.abduction.Definition;
import uk.co.mtford.jalp.abduction.logic.instance.*;
import uk.co.mtford.jalp.abduction.logic.instance.equalities.InEqualityInstance;
import uk.co.mtford.jalp.abduction.logic.instance.term.*;

public class JALPQueryParser implements JALPQueryParserConstants {

    public static List<IInferableInstance> readFromString(String string) throws ParseException {
        ByteArrayInputStream inputStream = null;
        byte[] buf = string.getBytes();
        inputStream = new ByteArrayInputStream(buf);
        JALPQueryParser parser = new JALPQueryParser(inputStream);
        return parser.AbductiveLogicQuery();
    }

  final public List<IInferableInstance> AbductiveLogicQuery() throws ParseException {
    LinkedList<IInferableInstance> goals = new LinkedList<IInferableInstance>();
    HashMap<String, VariableInstance> variablesSoFar = new HashMap<String, VariableInstance>();
    IInferableInstance goal;
    goal = Inferable(variablesSoFar);
      goals.add(goal);
    label_1:
    while (true) {
      if (jj_2_1(2)) {
        ;
      } else {
        break label_1;
      }
      jj_consume_token(COMMA);
      goal = Inferable(variablesSoFar);
          goals.add(goal);
    }
      {if (true) return goals;}
    throw new Error("Missing return statement in function");
  }

  final public IInferableInstance Inferable(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    IInferableInstance inferable;
    if (jj_2_2(2)) {
      inferable = PositiveInferable(variablesSoFar);
      {if (true) return inferable;}
    } else if (jj_2_3(2)) {
      inferable = NegativeInferable(variablesSoFar);
      {if (true) return inferable;}
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public IInferableInstance PositiveInferable(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    IInferableInstance inferable;
    if (jj_2_4(2)) {
      inferable = Predicate(variablesSoFar);
      {if (true) return inferable;}
    } else if (jj_2_5(2)) {
      inferable = Equality(variablesSoFar);
      {if (true) return inferable;}
    } else if (jj_2_6(2)) {
      inferable = InEquality(variablesSoFar);
      {if (true) return inferable;}
    } else if (jj_2_7(2)) {
      inferable = Constraint(variablesSoFar);
      {if (true) return inferable;}
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public NegationInstance NegativeInferable(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    IInferableInstance subformula;
    jj_consume_token(NOT);
    subformula = PositiveInferable(variablesSoFar);
      {if (true) return new NegationInstance(subformula);}
    throw new Error("Missing return statement in function");
  }

  final public PredicateInstance Predicate(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    Token t;
    String name;
    List<IUnifiableAtomInstance> parameters = new LinkedList<IUnifiableAtomInstance>();
    t = jj_consume_token(LCASENAME);
      name = t.image;
    if (jj_2_8(2)) {
      jj_consume_token(LBRACKET);
      parameters = ParameterList(variablesSoFar);
      jj_consume_token(RBRACKET);
    } else {
      ;
    }
      {if (true) return new PredicateInstance(name,parameters);}
    throw new Error("Missing return statement in function");
  }

  final public EqualityInstance Equality(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    IUnifiableAtomInstance left;
    IUnifiableAtomInstance right;
    left = Parameter(variablesSoFar);
    jj_consume_token(EQUALS);
    right = Parameter(variablesSoFar);
      {if (true) return new EqualityInstance(left,right);}
    throw new Error("Missing return statement in function");
  }

  final public InEqualityInstance InEquality(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    IUnifiableAtomInstance left;
    IUnifiableAtomInstance right;
    left = Parameter(variablesSoFar);
    jj_consume_token(NOTEQUALS);
    right = Parameter(variablesSoFar);
      {if (true) return new InEqualityInstance(left,right);}
    throw new Error("Missing return statement in function");
  }

  final public IConstraintInstance Constraint(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    IConstraintInstance instance;
    if (jj_2_9(2)) {
      instance = LessThanConstraint(variablesSoFar);
      {if (true) return instance;}
    } else if (jj_2_10(2)) {
      instance = LessThanEqConstraint(variablesSoFar);
      {if (true) return instance;}
    } else if (jj_2_11(2)) {
      instance = GreaterThanConstraint(variablesSoFar);
      {if (true) return instance;}
    } else if (jj_2_12(2)) {
      instance = GreaterThanEqConstraint(variablesSoFar);
      {if (true) return instance;}
    } else if (jj_2_13(2)) {
      instance = InListConstraint(variablesSoFar);
      {if (true) return instance;}
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public LessThanConstraintInstance LessThanConstraint(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    ITermInstance left;
    ITermInstance right;
    left = Term(variablesSoFar);
    jj_consume_token(LESSTHAN);
    right = Term(variablesSoFar);
      {if (true) return new LessThanConstraintInstance(left,right);}
    throw new Error("Missing return statement in function");
  }

  final public LessThanEqConstraintInstance LessThanEqConstraint(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    ITermInstance left;
    ITermInstance right;
    left = Term(variablesSoFar);
    jj_consume_token(LESSTHANEQ);
    right = Term(variablesSoFar);
      {if (true) return new LessThanEqConstraintInstance(left,right);}
    throw new Error("Missing return statement in function");
  }

  final public GreaterThanConstraintInstance GreaterThanConstraint(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    ITermInstance left;
    ITermInstance right;
    left = Term(variablesSoFar);
    jj_consume_token(GREATERTHAN);
    right = Term(variablesSoFar);
      {if (true) return new GreaterThanConstraintInstance(left,right);}
    throw new Error("Missing return statement in function");
  }

  final public GreaterThanEqConstraintInstance GreaterThanEqConstraint(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    ITermInstance left;
    ITermInstance right;
    left = Term(variablesSoFar);
    jj_consume_token(GREATERTHANEQ);
    right = Term(variablesSoFar);
      {if (true) return new GreaterThanEqConstraintInstance(left,right);}
    throw new Error("Missing return statement in function");
  }

  final public InListConstraintInstance InListConstraint(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    ITermInstance left;
    ITermInstance right;
    left = Term(variablesSoFar);
    jj_consume_token(IN);
    if (jj_2_14(2)) {
      right = CharConstantList(variablesSoFar);
      {if (true) return new InConstantListConstraintInstance(left,(CharConstantListInstance)right);}
    } else if (jj_2_15(2)) {
      right = IntegerConstantList(variablesSoFar);
      {if (true) return new InIntegerListConstraintInstance(left,(IntegerConstantListInstance)right);}
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public ITermInstance Term(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    ITermInstance term;
    if (jj_2_16(2)) {
      term = Variable(variablesSoFar);
      {if (true) return term;}
    } else if (jj_2_17(2)) {
      term = Constant();
      {if (true) return term;}
    } else if (jj_2_18(2)) {
      term = List(variablesSoFar);
      {if (true) return term;}
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public List<IUnifiableAtomInstance> ParameterList(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    LinkedList<IUnifiableAtomInstance> params = new LinkedList<IUnifiableAtomInstance>();
    IUnifiableAtomInstance param;
    if (jj_2_20(2)) {
      param = Parameter(variablesSoFar);
          params.add(param);
      label_2:
      while (true) {
        if (jj_2_19(2)) {
          ;
        } else {
          break label_2;
        }
        jj_consume_token(COMMA);
        param = Parameter(variablesSoFar);
              params.add(param);
      }
    } else {
      ;
    }
      {if (true) return params;}
    throw new Error("Missing return statement in function");
  }

  final public IUnifiableAtomInstance Parameter(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    IUnifiableAtomInstance unifiable;
    if (jj_2_21(2)) {
      unifiable = Variable(variablesSoFar);
          {if (true) return unifiable;}
    } else if (jj_2_22(2)) {
      unifiable = Constant();
          {if (true) return unifiable;}
    } else if (jj_2_23(2)) {
      unifiable = Predicate(variablesSoFar);
          {if (true) return unifiable;}
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public ListInstance List(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
   ListInstance listInstance;
    if (jj_2_24(2)) {
      listInstance = IntegerConstantList(variablesSoFar);
      {if (true) return listInstance;}
    } else if (jj_2_25(2)) {
      listInstance = CharConstantList(variablesSoFar);
      {if (true) return listInstance;}
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public IntegerConstantListInstance IntegerConstantList(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    IntegerConstantListInstance listInstance = new IntegerConstantListInstance();
    IntegerConstantInstance constantInstance;
    jj_consume_token(LSQBRACKET);
    constantInstance = IntegerConstant();
        listInstance.getList().add(constantInstance);
    label_3:
    while (true) {
      if (jj_2_26(2)) {
        ;
      } else {
        break label_3;
      }
      jj_consume_token(COMMA);
      constantInstance = IntegerConstant();
           listInstance.getList().add(constantInstance);
    }
    jj_consume_token(RSQBRACKET);
      {if (true) return listInstance;}
    throw new Error("Missing return statement in function");
  }

  final public CharConstantListInstance CharConstantList(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    CharConstantListInstance listInstance = new CharConstantListInstance();
    CharConstantInstance constantInstance;
    jj_consume_token(LSQBRACKET);
    constantInstance = CharConstant();
        listInstance.getList().add(constantInstance);
    label_4:
    while (true) {
      if (jj_2_27(2)) {
        ;
      } else {
        break label_4;
      }
      jj_consume_token(COMMA);
      constantInstance = CharConstant();
           listInstance.getList().add(constantInstance);
    }
    jj_consume_token(RSQBRACKET);
      {if (true) return listInstance;}
    throw new Error("Missing return statement in function");
  }

  final public VariableInstance Variable(HashMap<String, VariableInstance> variablesSoFar) throws ParseException {
    Token t;
    String name;
    t = jj_consume_token(UCASENAME);
        name = t.image;
        if (variablesSoFar.containsKey(name)) {
            {if (true) return variablesSoFar.get(name);}
        }
        else {
            VariableInstance variable = new VariableInstance(name);
            variablesSoFar.put(name,variable);
            {if (true) return variable;}
        }
    throw new Error("Missing return statement in function");
  }

  final public ConstantInstance Constant() throws ParseException {
    ConstantInstance constantInstance;
    if (jj_2_28(2)) {
      constantInstance = IntegerConstant();
      {if (true) return constantInstance;}
    } else if (jj_2_29(2)) {
      constantInstance = CharConstant();
      {if (true) return constantInstance;}
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public IntegerConstantInstance IntegerConstant() throws ParseException {
    Token t;
    Integer integer;
    t = jj_consume_token(INTEGER);
        integer = Integer.parseInt(t.image);
        {if (true) return new IntegerConstantInstance(integer);}
    throw new Error("Missing return statement in function");
  }

  final public CharConstantInstance CharConstant() throws ParseException {
    Token t;
    String name;
    t = jj_consume_token(LCASENAME);
        name = t.image;
        {if (true) return new CharConstantInstance(name);}
    throw new Error("Missing return statement in function");
  }

  private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  private boolean jj_2_5(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(4, xla); }
  }

  private boolean jj_2_6(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_6(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(5, xla); }
  }

  private boolean jj_2_7(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_7(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(6, xla); }
  }

  private boolean jj_2_8(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_8(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(7, xla); }
  }

  private boolean jj_2_9(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_9(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(8, xla); }
  }

  private boolean jj_2_10(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_10(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(9, xla); }
  }

  private boolean jj_2_11(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_11(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(10, xla); }
  }

  private boolean jj_2_12(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_12(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(11, xla); }
  }

  private boolean jj_2_13(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_13(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(12, xla); }
  }

  private boolean jj_2_14(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_14(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(13, xla); }
  }

  private boolean jj_2_15(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_15(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(14, xla); }
  }

  private boolean jj_2_16(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_16(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(15, xla); }
  }

  private boolean jj_2_17(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_17(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(16, xla); }
  }

  private boolean jj_2_18(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_18(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(17, xla); }
  }

  private boolean jj_2_19(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_19(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(18, xla); }
  }

  private boolean jj_2_20(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_20(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(19, xla); }
  }

  private boolean jj_2_21(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_21(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(20, xla); }
  }

  private boolean jj_2_22(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_22(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(21, xla); }
  }

  private boolean jj_2_23(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_23(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(22, xla); }
  }

  private boolean jj_2_24(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_24(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(23, xla); }
  }

  private boolean jj_2_25(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_25(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(24, xla); }
  }

  private boolean jj_2_26(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_26(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(25, xla); }
  }

  private boolean jj_2_27(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_27(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(26, xla); }
  }

  private boolean jj_2_28(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_28(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(27, xla); }
  }

  private boolean jj_2_29(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_29(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(28, xla); }
  }

  private boolean jj_3R_8() {
    if (jj_scan_token(LCASENAME)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_8()) jj_scanpos = xsp;
    return false;
  }

  private boolean jj_3R_16() {
    if (jj_3R_26()) return true;
    if (jj_scan_token(GREATERTHANEQ)) return true;
    return false;
  }

  private boolean jj_3_26() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_24()) return true;
    return false;
  }

  private boolean jj_3R_19() {
    if (jj_scan_token(LSQBRACKET)) return true;
    if (jj_3R_24()) return true;
    return false;
  }

  private boolean jj_3R_15() {
    if (jj_3R_26()) return true;
    if (jj_scan_token(GREATERTHAN)) return true;
    return false;
  }

  private boolean jj_3R_7() {
    if (jj_scan_token(NOT)) return true;
    if (jj_3R_6()) return true;
    return false;
  }

  private boolean jj_3R_25() {
    if (jj_scan_token(LCASENAME)) return true;
    return false;
  }

  private boolean jj_3_25() {
    if (jj_3R_18()) return true;
    return false;
  }

  private boolean jj_3_23() {
    if (jj_3R_8()) return true;
    return false;
  }

  private boolean jj_3R_22() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_24()) {
    jj_scanpos = xsp;
    if (jj_3_25()) return true;
    }
    return false;
  }

  private boolean jj_3_7() {
    if (jj_3R_11()) return true;
    return false;
  }

  private boolean jj_3_24() {
    if (jj_3R_19()) return true;
    return false;
  }

  private boolean jj_3_22() {
    if (jj_3R_21()) return true;
    return false;
  }

  private boolean jj_3_6() {
    if (jj_3R_10()) return true;
    return false;
  }

  private boolean jj_3R_14() {
    if (jj_3R_26()) return true;
    if (jj_scan_token(LESSTHANEQ)) return true;
    return false;
  }

  private boolean jj_3_21() {
    if (jj_3R_20()) return true;
    return false;
  }

  private boolean jj_3_5() {
    if (jj_3R_9()) return true;
    return false;
  }

  private boolean jj_3_4() {
    if (jj_3R_8()) return true;
    return false;
  }

  private boolean jj_3R_6() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_4()) {
    jj_scanpos = xsp;
    if (jj_3_5()) {
    jj_scanpos = xsp;
    if (jj_3_6()) {
    jj_scanpos = xsp;
    if (jj_3_7()) return true;
    }
    }
    }
    return false;
  }

  private boolean jj_3R_24() {
    if (jj_scan_token(INTEGER)) return true;
    return false;
  }

  private boolean jj_3_19() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_23()) return true;
    return false;
  }

  private boolean jj_3R_23() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_21()) {
    jj_scanpos = xsp;
    if (jj_3_22()) {
    jj_scanpos = xsp;
    if (jj_3_23()) return true;
    }
    }
    return false;
  }

  private boolean jj_3_3() {
    if (jj_3R_7()) return true;
    return false;
  }

  private boolean jj_3R_13() {
    if (jj_3R_26()) return true;
    if (jj_scan_token(LESSTHAN)) return true;
    return false;
  }

  private boolean jj_3_1() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_5()) return true;
    return false;
  }

  private boolean jj_3_2() {
    if (jj_3R_6()) return true;
    return false;
  }

  private boolean jj_3R_5() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_2()) {
    jj_scanpos = xsp;
    if (jj_3_3()) return true;
    }
    return false;
  }

  private boolean jj_3_29() {
    if (jj_3R_25()) return true;
    return false;
  }

  private boolean jj_3R_21() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_28()) {
    jj_scanpos = xsp;
    if (jj_3_29()) return true;
    }
    return false;
  }

  private boolean jj_3_20() {
    if (jj_3R_23()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_19()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3_28() {
    if (jj_3R_24()) return true;
    return false;
  }

  private boolean jj_3_13() {
    if (jj_3R_17()) return true;
    return false;
  }

  private boolean jj_3_12() {
    if (jj_3R_16()) return true;
    return false;
  }

  private boolean jj_3R_12() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_20()) jj_scanpos = xsp;
    return false;
  }

  private boolean jj_3_11() {
    if (jj_3R_15()) return true;
    return false;
  }

  private boolean jj_3_10() {
    if (jj_3R_14()) return true;
    return false;
  }

  private boolean jj_3R_11() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_9()) {
    jj_scanpos = xsp;
    if (jj_3_10()) {
    jj_scanpos = xsp;
    if (jj_3_11()) {
    jj_scanpos = xsp;
    if (jj_3_12()) {
    jj_scanpos = xsp;
    if (jj_3_13()) return true;
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3_9() {
    if (jj_3R_13()) return true;
    return false;
  }

  private boolean jj_3R_20() {
    if (jj_scan_token(UCASENAME)) return true;
    return false;
  }

  private boolean jj_3R_10() {
    if (jj_3R_23()) return true;
    if (jj_scan_token(NOTEQUALS)) return true;
    return false;
  }

  private boolean jj_3_18() {
    if (jj_3R_22()) return true;
    return false;
  }

  private boolean jj_3_17() {
    if (jj_3R_21()) return true;
    return false;
  }

  private boolean jj_3R_26() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_16()) {
    jj_scanpos = xsp;
    if (jj_3_17()) {
    jj_scanpos = xsp;
    if (jj_3_18()) return true;
    }
    }
    return false;
  }

  private boolean jj_3_16() {
    if (jj_3R_20()) return true;
    return false;
  }

  private boolean jj_3_27() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_25()) return true;
    return false;
  }

  private boolean jj_3R_9() {
    if (jj_3R_23()) return true;
    if (jj_scan_token(EQUALS)) return true;
    return false;
  }

  private boolean jj_3_15() {
    if (jj_3R_19()) return true;
    return false;
  }

  private boolean jj_3_14() {
    if (jj_3R_18()) return true;
    return false;
  }

  private boolean jj_3_8() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_3R_12()) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  private boolean jj_3R_17() {
    if (jj_3R_26()) return true;
    if (jj_scan_token(IN)) return true;
    return false;
  }

  private boolean jj_3R_18() {
    if (jj_scan_token(LSQBRACKET)) return true;
    if (jj_3R_25()) return true;
    return false;
  }

  /** Generated Token Manager. */
  public JALPQueryParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[0];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[29];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public JALPQueryParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public JALPQueryParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new JALPQueryParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public JALPQueryParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new JALPQueryParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public JALPQueryParser(JALPQueryParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(JALPQueryParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[22];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 0; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 22; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 29; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
            case 4: jj_3_5(); break;
            case 5: jj_3_6(); break;
            case 6: jj_3_7(); break;
            case 7: jj_3_8(); break;
            case 8: jj_3_9(); break;
            case 9: jj_3_10(); break;
            case 10: jj_3_11(); break;
            case 11: jj_3_12(); break;
            case 12: jj_3_13(); break;
            case 13: jj_3_14(); break;
            case 14: jj_3_15(); break;
            case 15: jj_3_16(); break;
            case 16: jj_3_17(); break;
            case 17: jj_3_18(); break;
            case 18: jj_3_19(); break;
            case 19: jj_3_20(); break;
            case 20: jj_3_21(); break;
            case 21: jj_3_22(); break;
            case 22: jj_3_23(); break;
            case 23: jj_3_24(); break;
            case 24: jj_3_25(); break;
            case 25: jj_3_26(); break;
            case 26: jj_3_27(); break;
            case 27: jj_3_28(); break;
            case 28: jj_3_29(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
