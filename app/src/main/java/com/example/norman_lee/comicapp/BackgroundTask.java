package com.example.norman_lee.comicapp;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*******
 * This class is an example of the template method design pattern.
 * The start method contains all code necessary to run one background task
 * and display its result on an Android activity.
 *
 * Subclass this as an inner class within your activity (why?)
 * Can you see the abstractions that are taking place?
 *
 * @param <I> input information needed to launch the background task
 * @param <O> outcome of the background task
 */

public abstract class BackgroundTask <I, O> {

    /*******
     * abstract method for the background task.
     * @param input - input necessary to launch the background task
     * @return the outcome of the background task
     */
    abstract public O task(I input);


    /************
     * abstract method for the task on the android UI thread
     * carried out after the background task is completed
     * @param result the result of the background task
     */
    abstract public void done(O result);


    /******
     * The algorithm to run one background task and display its result on an Android activity.
     * @param userInput the input necessary to launch the background task
     * */
    public void start(final I userInput) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());

        //how you post messages?
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //this is the background task --> the job is to download something
                //the things that would change is just the implementation
                final Container<O> container = new Container<>();
                //call abstract method task
                O o = task(userInput);
                container.set(o);

                //this is the UI task --> display it on the UI
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        O o = container.get();
                        if( o != null){
                            // to display on UI -> call abstract method called done
                            //when you have abstract class, you subclass it !!!!
                            done(o);
                        }
                        /** To implement subclass background task and provide implementation for task
                         * and done
                         * if requirement change -> subclass one more time and write new code
                         */
                    }
            });

    }


    /**********
     * Container class to allow anonymous inner class to read and write to local variables
     * @param <T> the object to be read and set
     */
    class Container<T> {        //make static or not? if static how?

        T value;

        Container() {
            this.value = null;
        }

        void set(T t) {
            this.value = t;
        }

        T get() {
            return this.value;
        }

    }

});}}
