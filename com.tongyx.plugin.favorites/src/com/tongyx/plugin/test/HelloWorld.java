package com.tongyx.plugin.test;


import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings({"deprecation","unused"})
public class HelloWorld {
	
	@FunctionalInterface
	interface Converter<F, T> {
	    T convert(F from);
	}


	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Hello World");
		shell.setBounds(100, 100, 500, 300);
		Layout layout = new FillLayout();
		shell.setLayout(layout);
		shell.setLayout(null);
		Label label = new Label(shell,SWT.CENTER | SWT.SHADOW_IN);
		label.setBounds(100, 100, 300, 200);
		label.setText("Hello World");
		Color red = new Color(display, 255, 0, 0);
		label.setForeground(red);
		shell.open();
		while(!shell.isDisposed()){
			if(!display.readAndDispatch()) display.sleep();
		}
		red.dispose();
		display.dispose();	
		ILabelProvider labelProvider;
		ITableLabelProvider tableLabelProvider;
		IStructuredContentProvider structuredContentProvider;
		ITreeContentProvider treeContentProvider;
//		structuredContentProvider.getElements(arg0);
//		structuredContentProvider.inputChanged(viewer, oldInput, newInput);
		ViewerSorter sorter;
		ViewerFilter filter;
		ListViewer listViewer;
		
		Converter<String,Integer> convert = Integer::valueOf;
		System.out.println(convert.convert("123")); 
		Converter<String,Integer>  convert1;
		convert1=(s)->Integer.valueOf(s);
		System.out.println(convert1.convert("456"));
	
	}

}
