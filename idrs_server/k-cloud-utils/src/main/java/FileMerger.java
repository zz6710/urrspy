import java.io.*;
import java.util.*;

public class FileMerger {
    public static void main(String[] args) {

        File diretory=new File("D:\\DEV\\code\\idrs-sit\\database\\dpb");
        Set excludeSet =new HashSet<String>();
        excludeSet.add("20230419(废弃不执行)");
        excludeSet.add("长沙");
        excludeSet.add("merge");
        String start_date ="20230629";
        String end_date ="20230712";
        List<File> newfileList= sort(diretory,excludeSet,start_date,end_date);
        String outputFileName = "D:\\DEV\\code\\idrs-sit\\database\\dpb\\merge\\merge"+start_date+"-"+end_date+".sql";
        mergeFiles(newfileList, outputFileName);
    }
    private static List<File> sort(File rootDirectory,Set excludeSet,String start_date,String end_date){
        List<File> newfileList = new ArrayList<File>();//
        File[] directorys=rootDirectory.listFiles();
        Arrays.sort(directorys);

        for (File directory : directorys){
            if(excludeSet.contains(directory.getName())) {
                System.out.println("跳过路径："+directory.getPath());
                continue;
            }

            if(start_date!=null && !start_date.isEmpty() && directory.getName().compareTo(start_date)<0 || end_date!=null && !end_date.isEmpty() && directory.getName().compareTo(end_date)>0 ) {
                System.out.println("不在选择范围内路径："+directory.getPath());
                continue;
            }
            File[] subs=directory.listFiles();
            Arrays.sort(subs);
            List<File> tab_fileList = new ArrayList<File>();//
            List<File> commit_fileList = new ArrayList<File>();//
            for (File sub : subs){
                if(sub.getName().startsWith("tab_")){
                    tab_fileList.add(sub);
                }else if (sub.getName().startsWith("commit_")){
                    commit_fileList.add(sub);
                }else{
                    throw new RuntimeException("脚本文件命名不规范");
                }

            }
            newfileList.addAll(tab_fileList);
            newfileList.addAll(commit_fileList);
            tab_fileList.clear();
        }
        return  newfileList;
    }

    private static void mergeFiles(List<File> fileList, String outputFileName) {
        try (FileWriter writer = new FileWriter(outputFileName)) {
            for (File file : fileList) {
                    System.out.println("读取路径："+file.getPath());
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    writer.write("-- "+file.getAbsolutePath()+" --");
                    writer.write(System.lineSeparator());
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.write(System.lineSeparator());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}