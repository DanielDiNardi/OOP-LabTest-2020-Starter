package ie.tudublin;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

public class Gantt extends PApplet
{	
	ArrayList<Task> tasks = new ArrayList<Task>();

	//task variables
		float taskX;
		float taskY;
		float divider;

		//lines and numbers variables
		int min;
		int max;
		float lineX;
		float lineY;
		float lineDiv;
		float lastLine;

		//box variables
		float colour;
		float offset;
		float selectedStart;
		float selectedEnd;
	
	public void settings()
	{
		size(800, 600);

		//task variables
		taskX = width * 0.05f;
		taskY = height * 0.15f;
		divider = 40;

		//lines and numbers variables
		min = 1;
		max = 30;
		lineX = width * 0.2f;
		lineY = height * 0.075f;
		lineDiv = 21;
		lastLine = map(mouseX, lineX, lineX + lineDiv * (max - 1), 1, 30);

		//box variables
		colour = 0;
		offset = 255 / 9;
		selectedStart = -1;
		selectedEnd = -1;
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
		selectedStart = -1;
		selectedEnd = -1;

		float y = taskY;

		for(int i = 0; i < tasks.size(); i++)
		{
			Task task = tasks.get(i);

			if(mouseX > map(task.getStart(), 1, 30, lineX, lineX + (lineDiv * (max - 1))) && 
			mouseX < map(task.getStart(), 1, 30, lineX, lineX + (lineDiv * (max - 1))) + 10f &&
			mouseY > y - divider / 2f &&
			mouseY < (divider - 5) + y - divider / 2f)
			{
				println("Mouse pressed");

				selectedStart = i;
			}
			else if(mouseX < map(task.getEnd(), 1, 30, lineX, lineX + (lineDiv * (max - 1))) && 
			mouseX > map(task.getEnd(), 1, 30, lineX, lineX + (lineDiv * (max - 1))) - 10f &&
			mouseY > y - divider / 2f &&
			mouseY < (divider - 5) + y - divider / 2f)
			{
				println("Mouse pressed");

				selectedEnd = i;
			}
			y += divider;
		}
	}

	public void mouseDragged()
	{
		println("Mouse dragged");

		if(selectedStart > -1)
		{
			Task task = tasks.get((int)selectedStart);

			//start moves left
			if(task.getStart() >= 2 && mouseX < map(task.getStart(), 1, 30, lineX, lineX + (lineDiv * (max - 1))))
			{
				int index = task.getStart() - 1;
				task.setStart(index);
			}

			//start moves right
			if(task.getStart() - task.getEnd() != -1 && mouseX > map(task.getStart(), 1, 30, lineX, lineX + (lineDiv * (max - 1))))
			{
				int index = task.getStart() + 1;
				task.setStart(index);
			}
		}
		else if(selectedEnd > -1)
		{
			Task task = tasks.get((int)selectedEnd);

			//end moves right
			if(task.getEnd() <= 29 && mouseX > map(task.getEnd(), 1, 30, lineX, lineX + (lineDiv * (max - 1))))
			{
				int index = task.getEnd() + 1;
				task.setEnd(index);
			}

			//end moves left
			if(task.getStart() - task.getEnd() != -1 && mouseX < map(task.getEnd(), 1, 30, lineX, lineX + (lineDiv * (max - 1))))
			{
				int index = task.getEnd() - 1;
				task.setEnd(index);
			}
		}
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
			rect(map(task.getStart(), 1, 30, lineX, lineX + (lineDiv * (max - 1))),
			taskY - divider / 2f,
			(task.getEnd() - task.getStart()) * lineDiv,
			divider - 5, 5f);

			taskY += divider;
			colour += offset;
		}
		taskY = height * 0.15f;
		colour = 0;
	}
}
