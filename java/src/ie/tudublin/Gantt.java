package ie.tudublin;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

public class Gantt extends PApplet
{	
	ArrayList<Task> tasks = new ArrayList<Task>();
	
	public void settings()
	{
		size(800, 600);
	}

	public void loadTasks()
	{
		Table t = loadTable("tasks.csv", "header");

		for(TableRow rows: t.rows())
		{
			Task fill = new Task(rows);
			tasks.add(fill);
		}
	}

	public void printTasks()
	{
		for(int i = 0; i < tasks.size(); i++)
		{
			println("Task: " + tasks.get(i));
		}
	}
	
	public void mousePressed()
	{
		println("Mouse pressed");	
	}

	public void mouseDragged()
	{
		println("Mouse dragged");
	}

	
	
	public void setup() 
	{
		colorMode(HSB);

		loadTasks();
		printTasks();
	}
	
	public void draw()
	{			
		background(0);

		displayTasks();
	}

	public void displayTasks()
	{
		//task variables
		float taskX = width * 0.05f;
		float taskY = height * 0.15f;
		float divider = 40;

		//lines and numbers variables
		int min = 1;
		int max = 30;
		float lineX = width * 0.2f;
		float lineY = height * 0.075f;
		float lineDiv = 21;

		float colour = 0;
		float offset = 255 / 9;

		//lines and numbers
		for(int i = min; i < max + 1; i++)
		{
			fill(255);
			textAlign(CENTER, CENTER);
			text(i, lineX, lineY / 2);

			if(i % 2 == 0)
			{
				stroke(100);
				line(lineX, lineY, lineX, height - lineY);
				lineX += lineDiv;
			}
			else
			{
				stroke(255);
				line(lineX, lineY, lineX, height - lineY);
				lineX += lineDiv;
			}
		}
		lineX = width * 0.2f;

		//task text and boxes
		for(Task task: tasks)
		{
			// text
			fill(255);
			textAlign(LEFT, CENTER);
			text(task.getTask(),taskX, taskY);

			//boxes
			noStroke();
			fill(colour, 255, 255);
			rect(map(task.getStart(), 1, 30, lineX, lineX + (lineDiv * (max - 1))), taskY - divider / 2f, (task.getEnd() - task.getStart()) * lineDiv, divider - 5, 5f);

			taskY += divider;
			colour += offset;
		}
	}
}
