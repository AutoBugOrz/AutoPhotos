package com.loungexi.pojo;

import javafx.application.Platform;

import java.util.function.IntPredicate;

/**
 * @author: LoungeXi
 **/
public class AsyncPlayer {
    private final IntPredicate intPredicate;
    //检查是否终止播放 是是否继续播放图片的唯一标识
    private Boolean isTerminate = false;
    //设置停止互相反复调用的停止标识
    private Integer flag = 0;

    public AsyncPlayer(IntPredicate intPredicate) {
        this.intPredicate = intPredicate;
        //首先调用play()方法 标识不通过开始播放图片
        play();
    }

    /**
     * @author: LoungeXi
     * @return: 创建线程播放图片
     **/
    private void play() {
        while (flag < 1) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //keepGo()方法中调整isTerminate的值 若isAutoPlay的值改变则isTerminate的值改变
                    keepGo();
                }
            });
            // flag自增使while条件判断不通过 实现play()方法和keepGo()方法停止互相调用来停止自动播放
            this.flag++;
        }

    }

    /**
     * @author: LoungeXi
     * @return: 检测并改变isTerminate的值，决定是否停止自动播放
     **/
    private void keepGo() {
        // 这里自减是为了在播放时保持flag的值不变 一旦停止播放play()方法中就会少keepGo()方法一次 flag自减少一次 则会导致flag=1让play()方法中while条件判断不通过 停止播放
        this.flag--;
        if (!isTerminate) {
            isTerminate = !intPredicate.test(1);
            if (!isTerminate) {
                play();
            }
        }
    }
}
