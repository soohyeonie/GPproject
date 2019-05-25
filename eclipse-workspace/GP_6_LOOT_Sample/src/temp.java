import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

//import InputSampleFrame.GameState;
//import InputSampleFrame.PlayerState;
import loot.*;
import loot.graphics.DrawableObject;

@SuppressWarnings("serial")
public class InputSampleFrame extends GameFrame {
	static final int lifebar_width = 160;	//HP �������� ���� ����
	static final int time_height = 400;
	static final int player_maxHP = 100;	//�÷��̾��� �ִ� HP		

	enum GameState {
		Started,	//������ ���� ���۵��� ���� ����
		Ready,		//�����̽� �ٸ� ���� ����, ���� ���� ������ ���۵�
		Running,	//������ ���۵� ����
		Finished,	//���ڰ� ������ ����
	}
		
	enum PlayerState {
		Left,
		Right,
		Stop
	}

	class Couple extends DrawableObject {
		public int score;
		public int level;		
		public Couple(int level) {
			x=100;
			y=100;
			width = 80;
			height = 80;
			if( level == 3 ) {
				score = 500;
				image = images.GetImage("player_normal");
			} else if( level == 2) {
				score = 100;
				image = images.GetImage("player_normal");
			} else {
				score = 50;
				image = images.GetImage("player_normal");
			}
		}

		//public boolean checkCollision() {
			//if( isPlayer1 == true )
		    //return !(p1.x > x + width || a.x + a.width < b.x || a.y > b.y + b.height || a.y + a.height < b.y);
		//}
	}
	
	class Solo extends DrawableObject {
		public int score;
		public boolean isPlayer1;
			
		public Solo(boolean isSuper) {
				
			if( isSuper == true ) {
				score = -300;
				image = images.GetImage("player_normal");
			} else {
				score = -50;
				image = images.GetImage("player_normal");
			}
		}
	}
		
	class Player extends DrawableObject {
		public PlayerState state;
		public int score;
		public int[] item;
		
		public Player(boolean isPlayer1) {
			state = PlayerState.Stop;
			score = 0;
			item = new int[3];
				
			if ( isPlayer1 == true ) {
				x = 350;
				y = 700;
				width = 80;
				height = 80;
			} else {
				x = 1050;
				y = 700;
				width = 80;
				height = 80;
			}
				
			image = images.GetImage("player_normal");
		}
	}

	class LifeBar extends DrawableObject {
		public LifeBar(boolean isForPlayer1) {
			if ( isForPlayer1 == true ) {
				x = 0;
				y = 10;
				width = lifebar_width;
				height = 20;
			} else {
				x = 390;
				y = 10;
				width = -lifebar_width;	//�ʺ� �Ǵ� ���̰��� ������ �׸��� �¿� �Ǵ� ���Ϸ� ������
				height = 20;
			}
			image = images.GetImage("lifebar_green");
		}
	}
	
	class time extends DrawableObject {
		int remaintime;
		public time() {
			remaintime=100;
			x=700;
			y=350;
			height = time_height;
			width = 50;
			image = images.GetImage("lifebar_green");
		}
	}
	
	Player p1;
	Player p2;
	GameState state = GameState.Started;	//�������� ��� Started -> Ready -> Running -> Finished -> Ready -> ...�� ������ ��ȯ��
	int numberOfWinner;						//1: 1P ��, 2: 2P ��, 3: ��� ��
	time gametime;
	Timer timer;
	Couple[] couples = new Couple[300];
	int couplestart=0;
	int coupleend=0;
	
	TimerTask timecheck = new TimerTask() {
		public void run() {
			gametime.remaintime-=1;
			gametime.height = time_height * gametime.remaintime / 100;
			//System.out.printf("%d\n",gametime.remaintime);
		}
	};
	/*
	TimerTask falling = new TimerTask() {
		public void run() {
			for(int i=couplestart;i<coupleend;i++) {
				if(couples[i]!=null) couples[i].y += 80;
			}
		}
	};
	TimerTask make = new TimerTask() {
		public void run() {
			couples[coupleend++] = new Couple(1);
			coupleend++;
		}
	};
	*/
	public InputSampleFrame(GameFrameSettings settings) {
		super(settings);
			
		images.LoadImage("Iages/player_normal.png", "player_normal");
		images.LoadImage("Images/player_guarding.png", "player_guarding");
		images.LoadImage("Images/player_punching.png", "player_punching");
		images.LoadImage("Images/lifebar_green.png", "lifebar_green");
		images.LoadImage("Images/lifebar_red.png", "lifebar_red");
			
		inputs.BindKey(KeyEvent.VK_D, 0);
		inputs.BindKey(KeyEvent.VK_F, 1);
		inputs.BindKey(KeyEvent.VK_K, 2);
		inputs.BindKey(KeyEvent.VK_L, 3);
		inputs.BindKey(KeyEvent.VK_SPACE, 4);
	}

	@Override
	public boolean Initialize() {
		p1 = new Player(true);
		p2 = new Player(false);

		gametime = new time();
		LoadColor(Color.black);
		LoadFont("�ü�ü 18");		//��ῡ �����ϰ� ���� �� �ֵ��� �ü�ü ���
			
		return true;
	}

	
	@Override
	public boolean Update(long timeStamp) {
		//�Է��� ��ư�� �ݿ�. �� �޼���� �׻� Update()�� ���� �κп��� ȣ���� �־�� ��
		inputs.AcceptInputs();
			
		switch ( state ) {
		case Ready:
			if ( inputs.buttons[4].IsReleasedNow() == true ) {
				
				timer = new Timer();
				timer.scheduleAtFixedRate(timecheck, 0, 1000);
				//timer.scheduleAtFixedRate(make, 0, 1000);
				//timer.scheduleAtFixedRate(falling, 500, 1000);
			}
			break;
		case Running:
			//�� �÷��̾��� ��ư �Է� ���� - ���⼭�� �� �� ������ �ִ� ��� �ƹ� �͵� �� ������ �ִ� ������ ����
			p1.state = PlayerState.Stop;
			p2.state = PlayerState.Stop;
		/*
			for(int i=couplestart;i<coupleend;i++) {
				if(couples[i].y > 700) {
					couples[i]=null;
					couplestart++;
				}
				
			}
			*/
			if( gametime.remaintime == 0 ) {
				timer.cancel();
				state = GameState.Finished;
			}
				
			if ( inputs.buttons[0].isPressed == true ) {
				if( p1.x>100 ) p1.x -= 5;
				p1.state = PlayerState.Left;
			}
			if ( inputs.buttons[1].isPressed == true ) {
				if( p1.x<600 ) p1.x += 5;
				p1.state = PlayerState.Right;
			}
			if ( inputs.buttons[0].isPressed == false && inputs.buttons[1].isPressed == false ) {
				p1.state = PlayerState.Stop;
			}
			
			if ( inputs.buttons[2].isPressed == true ) {
				if( p2.x>800 ) p2.x -= 5;
				p2.state = PlayerState.Left;
			}
			if ( inputs.buttons[3].isPressed == true ) {
				if( p2.x<1300 ) p2.x += 5;
				p2.state = PlayerState.Right;	
			}
			if ( inputs.buttons[2].isPressed == false && inputs.buttons[3].isPressed == false ) {
				p2.state = PlayerState.Stop;
			}
			break;

		case Started:
		case Finished:
			if ( inputs.buttons[4].IsPressedNow() == true ) {
				numberOfWinner = 0;
				p1.state = PlayerState.Stop;
				p2.state = PlayerState.Stop;
				//p1.score = 0;
				//p2.score = 0;
				state = GameState.Ready;
			}
			break;
		}

		switch ( p1.state )	{
		case Stop:
			p1.image = images.GetImage("player_normal");
			break;
		case Left:
			p1.image = images.GetImage("player_guarding");
			break;
		case Right:
			p1.image = images.GetImage("player_punching");
			break;
		}
		switch ( p2.state ) {
		case Stop:
			p2.image = images.GetImage("player_normal");
			break;
		case Left:
			p2.image = images.GetImage("player_guarding");
			break;
		case Right:
			p2.image = images.GetImage("player_punching");
			break;
		}
				
		return true;
	}

	@Override
	public void Draw(long timeStamp) {
		//�׸��� �۾� ���� - �� �޼���� Draw()�� ���� ������ �׻� ȣ���� �־�� ��
		BeginDraw();
			
		//ȭ���� �ٽ� �������� ä��
		ClearScreen();
			
		//p1.lifeBar.Draw(g);
		//p2.lifeBar.Draw(g);
		p1.Draw(g);
		p2.Draw(g);
		gametime.Draw(g);
		//if(p1.state == PlayerState.Left||p1.state == PlayerState.Right||p1.state == PlayerState.Stop)
		//p1.Draw(g);
		//if(p2.state == PlayerState.Left||p2.state == PlayerState.Right||p2.state == PlayerState.Stop)
		//p2.Draw(g);
		
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
		}
		
		//�׸��� �۾� �� - �� �޼���� Draw()�� ���� �Ʒ����� �׻� ȣ���� �־�� ��
		EndDraw();
	}
}
