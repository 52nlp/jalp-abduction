var data="{\"type\":\"A1RuleNode\",\"currentGoal\":\"has_colour(node1,C3)\",\"nextGoals\":[ \"has_colour(node2,D)\",\"ic(M,N,C) :- colour(C), has_colour(N,C), edge(N,M), has_colour(M,C).\"],\"assignments\":{},\"abducibles\":[],\"denials\":[],\"equalities\":[],\"constraints\":[],\"mark\":\"EXPANDED\",\"children\":[ {\"type\":\"A1RuleNode\",\"currentGoal\":\"has_colour(node2,D)\",\"nextGoals\":[ \"ic(M,N,C) :- colour(C), has_colour(N,C), edge(N,M), has_colour(M,C).\"],\"assignments\":{},\"abducibles\":[ \"has_colour(node1,C3)\"],\"denials\":[],\"equalities\":[],\"constraints\":[],\"mark\":\"EXPANDED\",\"children\":[ {\"type\":\"E1RuleNode\",\"currentGoal\":\"node1==node2\",\"nextGoals\":[ \"C3==D\",\"ic(M,N,C) :- colour(C), has_colour(N,C), edge(N,M), has_colour(M,C).\"],\"assignments\":{},\"abducibles\":[ \"has_colour(node1,C3)\"],\"denials\":[],\"equalities\":[],\"constraints\":[],\"mark\":\"EXPANDED\",\"children\":[ {\"type\":\"E1RuleNode\",\"currentGoal\":\"C3==D\",\"nextGoals\":[ \"ic(M,N,C) :- colour(C), has_colour(N,C), edge(N,M), has_colour(M,C).\"],\"assignments\":{},\"abducibles\":[ \"has_colour(node1,C3)\"],\"denials\":[],\"equalities\":[ \"node1==node2\"],\"constraints\":[],\"mark\":\"FAILED\",\"children\":[]}]},{\"type\":\"N1RuleNode\",\"currentGoal\":\"not(D==C3)\",\"nextGoals\":[ \"not(node2==node1)\",\"ic(M,N,C) :- colour(C), has_colour(N,C), edge(N,M), has_colour(M,C).\"],\"assignments\":{},\"abducibles\":[ \"has_colour(node1,C3)\",\"has_colour(node2,D)\"],\"denials\":[],\"equalities\":[],\"constraints\":[],\"mark\":\"EXPANDED\",\"children\":[ {\"type\":\"E2RuleNode\",\"currentGoal\":\"D==C3\",\"nextGoals\":[ \"not(node2==node1)\",\"ic(M,N,C) :- colour(C), has_colour(N,C), edge(N,M), has_colour(M,C).\"],\"nestedDenials\":[ \"ic :- .\"],\"assignments\":{},\"abducibles\":[ \"has_colour(node1,C3)\",\"has_colour(node2,D)\"],\"denials\":[],\"equalities\":[],\"constraints\":[],\"mark\":\"EXPANDED\",\"children\":[ {\"type\":\"PositiveFalseRuleNode\",\"currentGoal\":\"FALSE\",\"nextGoals\":[ \"not(node2==node1)\",\"ic(M,N,C) :- colour(C), has_colour(N,C), edge(N,M), has_colour(M,C).\"],\"assignments\":{ \"C3\":\"D\"},\"abducibles\":[ \"has_colour(node1,C3)\",\"has_colour(node2,D)\"],\"denials\":[],\"equalities\":[],\"constraints\":[],\"mark\":\"FAILED\",\"children\":[]}]}]}]}]}"