import java.lang.*;
import java.util.*;
public class Simplify_DFA {
    char[] stateArr;   //状态集的状态个数
    char[] alphabet;  //有穷字母表
    char[][] transform; //状态转换矩阵

    //private char initial;   //唯一初态
    char[] initialStateSet; //初始状态集合
    char[] finalStateSet;    //终止状态集合

    HashSet<HashSet> initialSet  = new HashSet<>();
    HashSet<HashSet> finalSet = new HashSet<>();
    HashSet set_1 = new HashSet();
    HashSet set_2 = new HashSet();


    void inputStateSet(){
        //输入有限状态集
        System.out.println("请输入五元式中的有限状态集：");
        Scanner readerChar = new Scanner(System.in);
        stateArr =  readerChar.nextLine().toCharArray();
    }
    void inputAlphabet(){
        //输入有穷字母表
        System.out.println("请输入五元式有穷字母表：");
        Scanner readerChar = new Scanner(System.in);
        String str = readerChar.nextLine();
        alphabet = str.toCharArray();
    }
    void inputTransform(){
        //输入状态矩阵
        System.out.println("请输入状态矩阵");
        transform = new char[stateArr.length][alphabet.length];
        Scanner readerChar = new Scanner(System.in);
        for(int i = 0; i< stateArr.length; i++) {
            for (int j = 0; j < alphabet.length; j++) {
                System.out.printf("状态"+stateArr[i]+"通过"+alphabet[j]+"转换为：");
                transform[i][j]= readerChar.next().charAt(0);
            }
            System.out.println();
        }
    }
    void inputInitialState(){
        //输入初态
        System.out.println("请输入初态:(输入“#”停止)");
        Scanner readerChar = new Scanner(System.in);
        String str = readerChar.nextLine().replaceAll("#","");
        initialStateSet = str.toCharArray();
    }
    void inputFinalState(){
        //输入终态
        System.out.println("请输入终态:(输入“#”停止)");
        Scanner readerChar = new Scanner(System.in);
        String str = readerChar.nextLine().replaceAll("#","");
        finalStateSet = str.toCharArray();
    }
    void outputTransform(){
        //输出状态转换矩阵
        System.out.println();
        for(int i = 0; i< stateArr.length; i++){
            if(i==0) {
                System.out.print("S\t");
                for (int k = 0; k < alphabet.length; k++) {
                    System.out.print(alphabet[k]);
                    System.out.print("\t");
                }
                System.out.println();
            }
            System.out.print(stateArr[i]);
            System.out.print("\t");
            for(int j=0;j < alphabet.length;j++){
                System.out.printf("%c",transform[i][j]);
                System.out.print("\t");
            }
            System.out.print("\n");
        }
    }


    //划分终态集合和非终态集合
    //将所划分的终态集合和非终态集合添加到集合initialSet中，此时initialSet中有两个元素。
    void partition(){
        for(char i:finalStateSet){      //构建终态集合
                set_1.add(i);
        }
        initialSet.add(new HashSet(set_1));
        for(char i:stateArr){
            if(!set_1.contains(i)) {    //判断状态i是否已经属于终态集合
                set_2.add(i);
            }
        }
        initialSet.add(new HashSet(set_2));
        set_1.clear();
        set_2.clear();
    }


    //获得指定的状态在状态数组里的下标索引
    public int getStateN(char c){
        for(int i = 0; i< stateArr.length; i++){
            if(stateArr[i]==c){
                return i;
            }
        }
        return -1;
    }
    //获得指定字母在字母表数组的下标索引
    public int getAlphabetN(char c){
        for(int i=0;i<alphabet.length;i++){
            if(alphabet[i]==c){
                return i;
            }
        }
        return -1;
    }

    //关键算法
    public void partitionAll(){
        for(char i:alphabet){
            System.out.println(i);
            while(!judgePartition(initialSet,this,i)){
                for(HashSet j :initialSet){
                    Iterator iterator = j.iterator();
                    int m = 1;
                    char change = ' ';
                    while(iterator.hasNext()){
                        char statue = (char)iterator.next();
                        if(m==1){
                            set_1.add(statue);
                            m=0;
                            change = transform[getStateN(statue)][getAlphabetN(i)];
                        }
                        else{
                            if(inSameSet(initialSet,change,transform[getStateN(statue)][getAlphabetN(i)])){
                                set_1.add(statue);
                            }
                            else
                                set_2.add(statue);
                        }
                    }
                    m=1;

                    finalSet.add(new HashSet(set_1));
                    finalSet.add(new HashSet(set_2));
                    set_1.clear();
                    set_2.clear();

                    Iterator <HashSet>iterator1 = finalSet.iterator();
                    while(iterator1.hasNext()){
                        if(iterator1.next().isEmpty()){
                            finalSet.remove(iterator1);
                        }
                    }
                    initialSet= new HashSet<>(finalSet);
                    finalSet.clear();
                }
            }
        }
    }

    //判断是否需要继续划分
    public boolean judgePartition(HashSet<HashSet> set, Simplify_DFA dfa, char c){
        int judge = 0;
        int signal =0;
        char k= ' ';
        for(HashSet h: set){
            System.out.println("1");
            Iterator iterator = h.iterator();
            while(iterator.hasNext()){
                char ch = (char)iterator.next();
                if(signal==0){
                    signal=1;
                    k=dfa.transform[dfa.getStateN(ch)][dfa.getAlphabetN(c)];
                }
                else{
                    if(!inSameSet(set,k,dfa.transform[dfa.getStateN(ch)][dfa.getAlphabetN(c)]))
                        return false;
                }
            }
            signal = 0;
        }
        System.out.println("true");
        return true;
    }
    //获取集合的首个字符
    public char getFirstChar(HashSet h){
        Iterator iterator = h.iterator();
        char c ='0';
        while(iterator.hasNext()){
            c=(char)iterator.next();
            break;
        }
        return c;
    }
    //判断两个字符是否在同一组集合中的同一个集合里
    public boolean inSameSet(HashSet<HashSet>H, char a, char b){
        if(a==b){
            return true;
        }
        else{
            boolean i=false;
            boolean j=false;
            for(HashSet h:H){
                Iterator iterator = h.iterator();
                while(iterator.hasNext()){
                    char c = (char)iterator.next();
                    if(c==a){
                        i=true;
                    }
                    else{
                        j=true;
                    }
                }
                if(i&&j) {
                    return true;
                }
                i=false;
                j=false;
                }
            }
            return false;
        }
}
