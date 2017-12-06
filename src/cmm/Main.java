package cmm;

public class Main {
    public static void main(String[] args){
        if (args.length != 1){
            System.out.println("Please enter: java -jar cmm.jar [file path]");
        }
        else {
            Parser.doParsing(args[0]);
        }
    }
}
