/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import model.Person;

/**
 *
 * @author jaret_000
 */
public interface ControllerClass {
    public abstract void preloadData(Person person);
    public abstract void preloadData(int personID);
}
