/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;


/**
 *
 * @author Yi
 */
public interface Converter<T> {
    public abstract Object[] convertToRow(T item);
    public abstract T convertFromRow(Object[] rowData);

}
