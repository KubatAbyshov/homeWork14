package com.company;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Main {


    public static void main(String[] args) {


   try {
            Semaphore sem1 = new Semaphore(1, true);

            CountDownLatch latch1 = new CountDownLatch(20);

            for (int i = 20; i <= 500; i = i + 20) {
                Upload upload = new Upload(sem1, latch1, i);
                upload.start();
                upload.join();
            }
            latch1.await();
       System.out.println("Файл загружен");
       System.out.println("_____________________");
       
        } catch (InterruptedException e) {

        }


        try {
            Semaphore sem2 = new Semaphore(3);
            CountDownLatch latch2 = new CountDownLatch(10);
            for (int i = 1; i <= 10; i++) {
                Downloader downloader = new Downloader(sem2, latch2, 100, i);
                downloader.start();

            }
            latch2.await();
            System.out.println("Файл удален из сервера");


        } catch (Exception ie) {
            ie.printStackTrace();

        }

    }
}


class Upload extends Thread {
    Semaphore sem;
    CountDownLatch latch;
    int up;


    public Upload(Semaphore sem, CountDownLatch latch, int up) {
        this.sem = sem;
        this.latch = latch;
        this.up = up;
    }

    public void run() {
        try {

            sem.acquire();
            System.out.println("Файл загружается - " + up + " мегабайт");
            sleep(1000);
            sem.release();

            sleep(1000);
            latch.countDown();



        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}


class Downloader extends Thread {
    Semaphore sem;
    CountDownLatch latch;
    int down;
    int id;

    public Downloader(Semaphore sem, CountDownLatch latch, int down, int id) {
        this.sem = sem;
        this.latch = latch;
        this.down = down;
        this.id = id;
    }


    public void run() {
        try {
            sem.acquire();
            System.out.println("Файл скачивается номером " + id + " - " + down + " мегабайт");
            sleep(1000);
            sem.release();

            sleep(1000);
            latch.countDown();


        } catch (InterruptedException e) {
        }

    }


}




