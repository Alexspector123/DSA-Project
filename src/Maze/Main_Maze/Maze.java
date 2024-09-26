package Maze.Main_Maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import Main.GamePanel;
import Maze.Map.Maze1;
import javax.swing.Timer;

import GameManage.Game;

public class Maze extends Game {
    GamePanel gamePanel;

    int level = 1;
    int score;
    
    public Maze(GamePanel gamePanel) {

        this.gamePanel = gamePanel;
    }
}
