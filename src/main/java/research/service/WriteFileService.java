package research.service;

import org.springframework.aop.ThrowsAdvice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFileService {

    public static void main(String[] args) throws IOException, InterruptedException {
       try{
           File file = new File("/Users/phong/working/research/optimize-performance-db/test.txt");
           file.createNewFile();

           FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
           BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

           bufferedWriter.write("hello");
           bufferedWriter.append("acasdasddasdasdasd");

           bufferedWriter.close();

       }catch (Exception ex) {
           ex.printStackTrace();
       }

//        Thread.sleep(5000000);
    }

}
