import java.awt.Color;
import java.awt.event.KeyEvent;

import loot.*;
import loot.graphics.DrawableObject;

/**
 * LOOT�� �Է� ����� ���������� Ȱ���� �����Դϴ�.
 * 
 * @author Racin
 *
 */
@SuppressWarnings("serial")
public class InputSampleFrame extends GameFrame
{
	/*
	 * ����:
	 * 
	 * Q:
	 * 		1P �ָ� ��������
	 * 
	 * A:
	 * 		1P ���
	 * 
	 * P:
	 * 		2P �ָ� ��������
	 * 
	 * L:
	 * 		2P ���
	 * 
	 * �����̽� ��:
	 * 
	 * 		�ٸ� Ű�� ������ ���� ���¿��� ������ ���� ���� ���� / �����
	 */
	
	/* -------------------------------------------
	 * 
	 * ��� �� ���� Ŭ���� ���� �κ�
	 * 
	 */
	
	static final int lifebar_width = 160;	//HP �������� ���� ����
	static final int player_maxHP = 100;	//�÷��̾��� �ִ� HP

	enum GameState
	{
		Started,	//������ ���� ���۵��� ���� ����
		Ready,		//�����̽� �ٸ� ���� ����, ���� ���� ������ ���۵�
		Running,	//������ ���۵� ����
		Finished,	//���ڰ� ������ ����
		BahnChick	//��Ģ�� ���� ������ ���� ����
	}
	
	enum PlayerState
	{
		Normal,		//�ƹ� ��ư�� �� ����
		Guarding,	//�����
		Punching	//�ָ��� ������
	}

	class Player extends DrawableObject
	{
		public PlayerState state;
		public int HP;
		public LifeBar lifeBar;
		
		public Player(boolean isPlayer1)
		{
			state = PlayerState.Normal;
			HP = player_maxHP;
			lifeBar = new LifeBar(isPlayer1);
			
			if ( isPlayer1 == true )
			{
				x = 0;
				y = 0;
				width = 300;
				height = 600;
			}
			else
			{
				x = 400;
				y = 0;
				width = -300;	//�ʺ� �Ǵ� ���̰��� ������ �׸��� �¿� �Ǵ� ���Ϸ� ������
				height = 600;
			}
			
			image = images.GetImage("player_normal");
		}
	}

	class LifeBar extends DrawableObject
	{
		public LifeBar(boolean isForPlayer1)
		{
			if ( isForPlayer1 == true )
			{
				x = 10;
				y = 10;
				width = lifebar_width;
				height = 20;
			}
			else
			{
				x = 390;
				y = 10;
				width = -lifebar_width;	//�ʺ� �Ǵ� ���̰��� ������ �׸��� �¿� �Ǵ� ���Ϸ� ������
				height = 20;
			}
			
			image = images.GetImage("lifebar_green");
		}
	}
	
	
	/* -------------------------------------------
	 * 
	 * �ʵ� ���� �κ�
	 * 
	 */

	Player p1;
	Player p2;
	GameState state = GameState.Started;	//�������� ��� Started -> Ready -> Running -> Finished -> Ready -> ...�� ������ ��ȯ��
	int numberOfWinner;						//1: 1P ��, 2: 2P ��, 3: ��� ��

	
	/* -------------------------------------------
	 * 
	 * �޼��� ���� �κ�
	 * 
	 */

	public InputSampleFrame(GameFrameSettings settings)
	{
		super(settings);
		
		images.LoadImage("Images/player_normal.png", "player_normal");
		images.LoadImage("Images/player_guarding.png", "player_guarding");
		images.LoadImage("Images/player_punching.png", "player_punching");
		images.LoadImage("Images/lifebar_green.png", "lifebar_green");
		images.LoadImage("Images/lifebar_red.png", "lifebar_red");
		
		inputs.BindKey(KeyEvent.VK_Q, 0);
		inputs.BindKey(KeyEvent.VK_A, 1);
		inputs.BindKey(KeyEvent.VK_P, 2);
		inputs.BindKey(KeyEvent.VK_L, 3);
		inputs.BindKey(KeyEvent.VK_SPACE, 4);
	}

	@Override
	public boolean Initialize()
	{
		p1 = new Player(true);
		p2 = new Player(false);
		
		LoadColor(Color.black);
		LoadFont("�ü�ü 18");		//��ῡ �����ϰ� ���� �� �ֵ��� �ü�ü ���
		
		return true;
	}

	@Override
	public boolean Update(long timeStamp)
	{
		//�Է��� ��ư�� �ݿ�. �� �޼���� �׻� Update()�� ���� �κп��� ȣ���� �־�� ��
		inputs.AcceptInputs();
		
		switch ( state )
		{
		case Ready:
			//�غ� ���¿��� �÷��̾ ��ư�� ������ ��Ģ
			if ( inputs.buttons[0].isPressed == true ||
				 inputs.buttons[1].isPressed == true ||
				 inputs.buttons[2].isPressed == true ||
				 inputs.buttons[3].isPressed == true )
			{
				//���� ��ư�� �������� Ȯ�� - �� ���� ������ ���� �켱 �����
				p1.state = PlayerState.Normal;
				p2.state = PlayerState.Normal;
				
				if ( inputs.buttons[0].isPressed == true )
				{
					p1.state = PlayerState.Punching;
					numberOfWinner += 2;
				}
				
				else if ( inputs.buttons[1].isPressed == true )
				{
					p1.state = PlayerState.Guarding;
					numberOfWinner += 2;
				}

				if ( inputs.buttons[2].isPressed == true )
				{
					p2.state = PlayerState.Punching;
					numberOfWinner += 1;
				}
				
				else if ( inputs.buttons[3].isPressed == true )
				{
					p2.state = PlayerState.Guarding;
					numberOfWinner += 1;
				}
				
				state = GameState.BahnChick;
			}
			//�� �÷��̾ ��ư�� ������ ���� ���¿��� �����̽� �ٸ� ���� ���� ����
			else if ( inputs.buttons[4].IsReleasedNow() == true )
				state = GameState.Running;
			break;
		case Running:
			//�� �÷��̾��� ��ư �Է� ���� - ���⼭�� �� �� ������ �ִ� ��� �ƹ� �͵� �� ������ �ִ� ������ ����
			p1.state = PlayerState.Normal;
			p2.state = PlayerState.Normal;
			
			if ( inputs.buttons[0].isPressed == true && inputs.buttons[1].isPressed == false )
				p1.state = PlayerState.Punching;
			
			if ( inputs.buttons[0].isPressed == false && inputs.buttons[1].isPressed == true )
				p1.state = PlayerState.Guarding;

			if ( inputs.buttons[2].isPressed == true && inputs.buttons[3].isPressed == false )
				p2.state = PlayerState.Punching;
			
			if ( inputs.buttons[2].isPressed == false && inputs.buttons[3].isPressed == true )
				p2.state = PlayerState.Guarding;
			
			/*
			 * ���� ����
			 * 
			 * ���� �ָ��� �������� ��
			 * 		��밡 ������̾��ٸ� �� ü���� 0���� ����
			 * 		�׷��� �ʴٸ� ����� ü���� 0���� ����
			 */
			
			if ( p1.state == PlayerState.Punching )
			{
				if ( p2.state == PlayerState.Guarding )
					p1.HP = 0;
				
				else
					p2.HP = 0;
			}
			
			if ( p2.state == PlayerState.Punching )
			{
				if ( p1.state == PlayerState.Guarding )
					p2.HP = 0;
				
				else
					p1.HP = 0;
			}
			

			/*
			 * ��� �г�Ƽ ����
			 * 
			 * ������̾��ٸ� ü���� 1��ŭ ���ҽ�Ŵ - �ּҰ��� 0
			 */
			
			if ( p1.state == PlayerState.Guarding && p1.HP != 0 )
				--p1.HP;
			
			if ( p2.state == PlayerState.Guarding && p2.HP != 0 )
				--p2.HP;
			
			
			/*
			 * ü���� 0�� �� �÷��̾ �ִ� ��� ���� ��
			 * 
			 * �� �� ���ÿ� 0�� �� ��� �� �� �� ������ ����
			 */
			if ( p1.HP == 0 )
			{
				numberOfWinner += 2;
				state = GameState.Finished;
			}

			if ( p2.HP == 0 )
			{
				numberOfWinner += 1;
				state = GameState.Finished;
			}
			break;
		case Started:
		case Finished:
		case BahnChick:
			//�ٸ� ��ư�� ������ ���� ���¿��� �����̽� �ٸ� ������ ���� �غ� 
			if ( inputs.buttons[0].isPressed == false &&
				 inputs.buttons[1].isPressed == false &&
				 inputs.buttons[2].isPressed == false &&
				 inputs.buttons[3].isPressed == false &&
				 inputs.buttons[4].IsPressedNow() == true )
			{
				numberOfWinner = 0;
				p1.state = PlayerState.Normal;
				p2.state = PlayerState.Normal;
				p1.HP = player_maxHP;
				p2.HP = player_maxHP;
				state = GameState.Ready;
			}
			break;
		}

		/*
		 * �׸��⿡ ����� �ʵ� �缳��
		 */
		
		switch ( p1.state )
		{
		case Normal:
			p1.image = images.GetImage("player_normal");
			break;
		case Guarding:
			p1.image = images.GetImage("player_guarding");
			break;
		case Punching:
			p1.image = images.GetImage("player_punching");
			break;
		}
		
		switch ( p2.state )
		{
		case Normal:
			p2.image = images.GetImage("player_normal");
			break;
		case Guarding:
			p2.image = images.GetImage("player_guarding");
			break;
		case Punching:
			p2.image = images.GetImage("player_punching");
			break;
		}
		
		p1.lifeBar.width = p1.HP * lifebar_width / player_maxHP;
		p2.lifeBar.width = -1 * p2.HP * lifebar_width / player_maxHP;
		
		if ( p1.HP < player_maxHP / 2 )
			p1.lifeBar.image = images.GetImage("lifebar_red");
		else
			p1.lifeBar.image = images.GetImage("lifebar_green");
		
		if ( p2.HP < player_maxHP / 2 )
			p2.lifeBar.image = images.GetImage("lifebar_red");
		else
			p2.lifeBar.image = images.GetImage("lifebar_green");
			
		
		return true;
	}

	@Override
	public void Draw(long timeStamp)
	{
		//�׸��� �۾� ���� - �� �޼���� Draw()�� ���� ������ �׻� ȣ���� �־�� ��
		BeginDraw();
		
		//ȭ���� �ٽ� �������� ä��
		ClearScreen();
		
		p1.lifeBar.Draw(g);
		p2.lifeBar.Draw(g);
		
		if ( p1.state == PlayerState.Punching )
		{
			p2.Draw(g);
			p1.Draw(g);
		}
		else
		{
			p1.Draw(g);
			p2.Draw(g);
		}
		
		switch ( state )
		{
		case Started:
			DrawString(10, 50, "    1P�� Q�� A, 2P�� P�� L�� �����Ѵ�.    ");
			DrawString(10, 72, "�ٸ� Ű ���� �����̽� �ٸ� ������ �����Ѵ�.");
			break;
		case Ready:
			DrawString(10, 50, "       �����̽� �ٸ� ���� �����Ѵ�.       ");
			DrawString(10, 72, "      ���� �ٸ� Ű�� ������ ��Ģ�̴�.      ");
			break;
		case Running:
			DrawString(10, 72, "                 �����ߴ�.                 ");
			break;
		case Finished:
			if ( numberOfWinner == 3 )
				DrawString(10, 50, "                �� �� ����.               ");
			else
				DrawString(10, 50, "               %dP�� �̰��.               ", numberOfWinner);
			
			DrawString(10, 72, "     �����̽� �ٸ� ������ �ٽ� �����Ѵ�.   ");
			break;
		case BahnChick:
			if ( numberOfWinner == 3 )
				DrawString(10, 50, "            �� �� ��Ģ�� ���ߴ�.          ");
			else
				DrawString(10, 50, "            %dP�� ��Ģ�� ���ߴ�.           ", 3 - numberOfWinner);
			
			DrawString(10, 72, "     �����̽� �ٸ� ������ �ٽ� �����Ѵ�.   ");
			break;
		}
		
		//�׸��� �۾� �� - �� �޼���� Draw()�� ���� �Ʒ����� �׻� ȣ���� �־�� ��
		EndDraw();
	}

}
