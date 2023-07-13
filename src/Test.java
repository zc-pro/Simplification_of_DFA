import java.util.*;
import java.lang.*;
public class Test {
    public static void main(String[] args){
        System.out.println("————此程序用于DFA的最简化————");
        Scanner scanner = new Scanner(System.in);
        String s = null;
        //初始化并创建DFA对象

        Simplify_DFA dfa = new Simplify_DFA();
        //打印DFA
        dfa.inputStateSet();
        dfa.inputAlphabet();
        dfa.inputTransform();
        dfa.inputInitialState();
        dfa.inputFinalState();
        dfa.partition();

        for(HashSet h:dfa.initialSet) {
            System.out.println(h);
        }
        dfa.partitionAll();
    }
}



