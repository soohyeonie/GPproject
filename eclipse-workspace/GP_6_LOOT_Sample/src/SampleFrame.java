import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import loot.GameFrame;
import loot.GameFrameSettings;
import loot.graphics.DrawableObject;
import loot.graphics.DrawableObject3D;
import loot.graphics.Layer;
import loot.graphics.RotatableLayer;
import loot.graphics.Viewport;

public class SampleFrame extends GameFrame
{
	/*
	 * state:
	 * 
	 * 0. 시작
	 * 
	 * 1. 진행
	 * 
	 * 2. 종료
	 * 
	 */
	int state = 0;
	int nextState = 0;
	
	Viewport[] viewports = new Viewport[3];
	
	DrawableObject bg_start;
	DrawableObject bg_process;
	DrawableObject bg_end;

	
	class Player extends RotatableLayer
	{
		public boolean isSwingingLeft = false;
		
		public DrawableObject obj_body;
		public DrawableObject obj_tail;
		
		public Player(int x, int y)
		{
			super(x, y, 30, 100);
			
			this.rotate_origin_x = 0.5;
			this.rotate_origin_y = 0.85;
			
			obj_body = new DrawableObject(0, 70, 30, 30, images.GetImage("bg_start"));
			this.children.add(obj_body);
			obj_tail = new DrawableObject(0, 0, 5, 70, images.GetImage("bg_end"));
			this.children.add(obj_tail);
		}
	}
	
	Player player;
	
	
	public SampleFrame(GameFrameSettings settings)
	{
		super(settings);
		
		images.CreateTempImage(Color.BLACK, "bg_start");
		images.CreateTempImage(Color.BLUE, "bg_process");
		images.CreateTempImage(Color.RED, "bg_end");
	}

	@Override
	public boolean Initialize()
	{
		bg_start = new DrawableObject(0, 0, settings.canvas_width, settings.canvas_height, images.GetImage("bg_start"));
		viewports[0] = new Viewport(0, 0, settings.canvas_width, settings.canvas_height);
		viewports[0].children.add(bg_start);

		player = new Player(300, 200);
		viewports[1] = new Viewport(0, 0, settings.canvas_width, settings.canvas_height);
		viewports[1].children.add(player);	

		bg_end = new DrawableObject(0, 0, settings.canvas_width, settings.canvas_height, images.GetImage("bg_end"));
		viewports[2] = new Viewport(0, 0, settings.canvas_width, settings.canvas_height);
		viewports[2].children.add(bg_end);

		
		
		inputs.BindKey(KeyEvent.VK_ENTER, 0);

		return true;
	}

	@Override
	public boolean Update(long timeStamp)
	{
		inputs.AcceptInputs();

		// 입력 처리
		switch ( state )
		{
		case 0:
			if ( inputs.buttons[0].IsPressedNow() )
			{
				nextState = 1;
			}
			break;
		case 1:
			if ( inputs.buttons[0].IsPressedNow() )
			{
				nextState = 2;
			}			
			break;
		case 2:
			if ( inputs.buttons[0].IsPressedNow() )
			{
				nextState = 0;
			}
			break;
		}
		
		
		
		// 게임 진행
		switch ( state )
		{
		case 0:
			break;
		case 1:
			if ( player.isSwingingLeft )
			{
				player.obj_tail.x -= 2;
				
				if ( player.obj_tail.x == 0 )
					player.isSwingingLeft = false;
			}
			else
			{
				player.obj_tail.x += 2;
				
				if ( player.obj_tail.x == 24 )
					player.isSwingingLeft = true;
			}
			
			player.angle += 0.01;
			
			break;
		case 2:
			break;
		}
		
		

		// 상태 전환시 실행할 코드 실행
		if ( state != nextState )
		{
			switch ( state )
			{
			case 0:
				System.out.println("이제 곧 state 0을 떠남");
				break;
			case 1:
				System.out.println("이제 곧 state 1을 떠남");
				break;
			case 2:
				System.out.println("이제 곧 state 2를 떠남");
				break;
			}

			switch ( nextState )
			{
			case 0:
				System.out.println("이제 곧 state 0에 진입함");
				break;
			case 1:
				System.out.println("이제 곧 state 1에 진입함");
				break;
			case 2:
				System.out.println("이제 곧 state 2에 진입함");
				break;
			}
		}
		
		
		return true;
	}

	@Override
	public void Draw(long timeStamp)
	{
		BeginDraw();
		ClearScreen();
		
		viewports[state].Draw(g);

		state = nextState;
		
		EndDraw();
	}

}
