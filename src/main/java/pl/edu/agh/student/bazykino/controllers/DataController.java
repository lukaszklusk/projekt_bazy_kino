package pl.edu.agh.student.bazykino.controllers;

import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@RestController
public class DataController {

    //Test method
    @GetMapping("/img")
    public String getBase64Img(){
        File file = new File("./testimage.png");
        byte[] bytes = new byte[(int) file.length()];
        try(FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(bytes);
    }
}
