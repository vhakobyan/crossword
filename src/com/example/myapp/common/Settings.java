package com.example.myapp.common;

import com.example.myapp.common.api.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Settings {

    public static String FILE_NAME = ".crossword";
    public static boolean soundEnabled = true;
    public static int[] highScores = new int[]{100, 80, 50, 30, 10};

    public static void load(FileIO files) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(files.readFile(FILE_NAME)));
            soundEnabled = Boolean.parseBoolean(in.readLine());
            for (int i = 0; i < 5; i++) {
                highScores[i] = Integer.parseInt(in.readLine());
            }
        } catch (Exception ignored) {
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException ignored) {}
        }
    }

    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(FILE_NAME)));
            out.write(Boolean.toString(soundEnabled));
            for (int i = 0; i < 5; i++) {
                out.write(Integer.toString(highScores[i]));
            }

        } catch (Exception ignored) {
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException ignored) {}
        }
    }

    public static void addScore(int score) {
        for (int i = 0; i < 5; i++) {
            if (highScores[i] < score) {
                for (int j = 4; j > i; j--)
                    highScores[j] = highScores[j - 1];
                highScores[i] = score;
                break;
            }
        }
    }

}
