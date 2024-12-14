package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    GamePanel gamePanel;
    public Config(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }    
    public void saveConfig() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));
            bw.newLine();

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadConfig() {
        
        try {
            BufferedReader br = new BufferedReader(new FileReader("Config.txt"));

            String s = br.readLine();

            br.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}