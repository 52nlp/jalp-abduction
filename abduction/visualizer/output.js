var data="{\"type\":\"D1RuleNode\",\"currentGoal\":\"p(X3)\",\"nextGoals\":[],\"assignments\":{},\"abducibles\":[],\"denials\":[],\"equalities\":[],\"constraints\":[],\"mark\":\"EXPANDED\",\"children\":[ {\"type\":\"E1RuleNode\",\"currentGoal\":\"X3==X4\",\"nextGoals\":[ \"a(X4)\",\"not(q(X4))\"],\"assignments\":{},\"abducibles\":[],\"denials\":[],\"equalities\":[],\"constraints\":[],\"mark\":\"EXPANDED\",\"children\":[ {\"type\":\"A1RuleNode\",\"currentGoal\":\"a(X4)\",\"nextGoals\":[ \"not(q(X4))\"],\"assignments\":{ \"X3\":\"X4\"},\"abducibles\":[],\"denials\":[],\"equalities\":[ \"X3==X4\"],\"constraints\":[],\"mark\":\"EXPANDED\",\"children\":[ {\"type\":\"N1RuleNode\",\"currentGoal\":\"not(q(X4))\",\"nextGoals\":[],\"assignments\":{ \"X3\":\"X4\"},\"abducibles\":[ \"a(X4)\"],\"denials\":[],\"equalities\":[ \"X3==X4\"],\"constraints\":[],\"mark\":\"EXPANDED\",\"children\":[ {\"type\":\"D2RuleNode\",\"currentGoal\":\"q(X4)\",\"nextGoals\":[],\"nestedDenials\":[ \"ic :- .\"],\"assignments\":{ \"X3\":\"X4\"},\"abducibles\":[ \"a(X4)\"],\"denials\":[],\"equalities\":[ \"X3==X4\"],\"constraints\":[],\"mark\":\"EXPANDED\",\"children\":[ {\"type\":\"E2RuleNode\",\"currentGoal\":\"X4==1\",\"nextGoals\":[],\"nestedDenials\":[ \"ic :- .\"],\"assignments\":{ \"X3\":\"X4\"},\"abducibles\":[ \"a(X4)\"],\"denials\":[],\"equalities\":[ \"X3==X4\"],\"constraints\":[],\"mark\":\"EXPANDED\",\"children\":[ {\"type\":\"PositiveTrueRuleNode\",\"currentGoal\":\"TRUE\",\"nextGoals\":[],\"assignments\":{ \"X3\":\"X4\"},\"abducibles\":[ \"a(X4)\"],\"denials\":[],\"equalities\":[ \"X3==X4\",\"X4!=1\"],\"constraints\":[],\"mark\":\"FAILED\",\"children\":[]},{\"type\":\"PositiveFalseRuleNode\",\"currentGoal\":\"FALSE\",\"nextGoals\":[],\"assignments\":{ \"X3\":\"X4\",\"X4\":\"1\"},\"abducibles\":[ \"a(X4)\"],\"denials\":[],\"equalities\":[ \"X3==X4\"],\"constraints\":[],\"mark\":\"FAILED\",\"children\":[]}]}]}]}]}]}]}"