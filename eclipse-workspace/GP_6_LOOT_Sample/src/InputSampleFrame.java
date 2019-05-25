import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//import InputSampleFrame.PlayerState;
//import InputSampleFrame.GameState;
//import InputSampleFrame.PlayerState;
import loot.*;
import loot.graphics.DrawableObject;
import loot.graphics.Viewport;

@SuppressWarnings("serial")
public class InputSampleFrame extends GameFrame {
	static final int lifebar_width = 160;	//HP 게이지의 가로 길이
	static final int time_height = 400;
	static final int player_maxHP = 100;	//플레이어의 최대 HP		

	enum GameState {
		Base, //메인화면
		Gamerole, //게임설명
		Naming,		//닉네임 입력
		CheckRanking,//랭킹확인
		Ending,		//엔딩
		Started,	//
		Ready,		//Ready, Go
		Running,	//게임이 시작된 상태
		Finished,	//승자가 결정된 상태
	}
		
	enum PlayerState {
		Left,
		Right,
		Stop
	}
	enum Button {
		Check,
		Back,
		Gamerole,
		Start
	}
	class Couple extends DrawableObject {
		public int score;
		public int level;
		public int direction;
		public Couple(int level) {
			if(randomGenerator.nextInt(2)%2==0) {
				direction = -1;
			} else {
				direction = 1;
			}
			x=100+randomGenerator.nextInt(500);
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
				
			image = images.GetImage("player");
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
	
	GameState state = GameState.Base;	//정상적인 경우 Started -> Ready -> Running -> Finished -> Ready -> ...의 순서로 전환됨
	int numberOfWinner;						//1: 1P 승, 2: 2P 승, 3: 모두 패
	time gametime;
	Timer timer;
	Couple[] couples = new Couple[300];
	int couplestart=0;
	int coupleend=0;
	Random randomGenerator;
	Viewport[] viewports = new Viewport[6];


	TimerTask timecheck  = new TimerTask() {
		public void run() {
			gametime.remaintime-=1;
			gametime.height = time_height * gametime.remaintime / 100;
			//System.out.printf("%d\n",gametime.remaintime);
		}
	};
	
	TimerTask falling = new TimerTask() {
		public void run() {
			for(int i=couplestart;i<coupleend;i++) {
				if(couples[i]!=null) couples[i].y += 80;
			}
		}
	};
	TimerTask makecouple1 = new TimerTask() {
		public void run() {
			couples[coupleend] = new Couple(1);
			coupleend++;
		}
	};
	TimerTask makecouple2 = new TimerTask() {
		public void run() {
			couples[coupleend]=new Couple(2);
			coupleend++;
		}
	};
	TimerTask makecouple3 = new TimerTask() {
		public void run() {
			couples[coupleend]=new Couple(3);
			coupleend++;
		}
	};
	public InputSampleFrame(GameFrameSettings settings) {
		super(settings);
		images.LoadImage("Images/back.png", "back");
		images.LoadImage("Images/chkrank.png", "chkrank");
		images.LoadImage("Images/gamerole.png", "gamerole");
		images.LoadImage("Images/start.png", "start");
		
		images.LoadImage("Images/player.png", "player");
		images.LoadImage("Images/player_normal.png", "player_normal");
		images.LoadImage("Images/player_guarding.png", "player_guarding");
		images.LoadImage("Images/player_punching.png", "player_punching");
		images.LoadImage("Images/lifebar_green.png", "lifebar_green");
		images.LoadImage("Images/lifebar_red.png", "lifebar_red");
		
		images.CreateTempImage(Color.BLACK, "bg_base");
		images.CreateTempImage(Color.YELLOW, "bg_ex");
		images.CreateTempImage(Color.RED, "bg_naming");
		images.CreateTempImage(Color.WHITE, "bg_chk");
		images.CreateTempImage(Color.GREEN, "bg_end");
		images.CreateTempImage(Color.ORANGE, "bg_game");
			
		inputs.BindKey(KeyEvent.VK_D, 0);
		inputs.BindKey(KeyEvent.VK_F, 1);
		inputs.BindKey(KeyEvent.VK_K, 2);
		inputs.BindKey(KeyEvent.VK_L, 3);
		inputs.BindKey(KeyEvent.VK_SPACE, 4);
		inputs.BindMouseButton(MouseEvent.BUTTON1,5);
	}

	@Override
	public boolean Initialize() {
		p1 = new Player(true);
		p2 = new Player(false);
		randomGenerator = new Random();
		//gametime = new time();
		LoadColor(Color.black);
		LoadFont("궁서체 18");		//대결에 진지하게 임할 수 있도록 궁서체 사용

		DrawableObject bt_back = new DrawableObject(1050, 650, 200, 100, images.GetImage("back"));
		DrawableObject bt_chkrank = new DrawableObject(950, 600, 200, 100, images.GetImage("chkrank"));
		DrawableObject bt_gamerole = new DrawableObject(600, 600, 200, 100, images.GetImage("gamerole"));
		DrawableObject bt_start = new DrawableObject(250, 600, 200, 100, images.GetImage("start"));		
		
		DrawableObject bg_base = new DrawableObject(0, 0, settings.canvas_width, settings.canvas_height, images.GetImage("bg_base"));
		viewports[0] = new Viewport(0, 0, settings.canvas_width, settings.canvas_height);
		viewports[0].children.add(bt_start);
		viewports[0].children.add(bt_chkrank);
		viewports[0].children.add(bt_gamerole);
		viewports[0].children.add(bg_base);
		
		DrawableObject bg_ex = new DrawableObject(0, 0, settings.canvas_width, settings.canvas_height, images.GetImage("bg_ex"));
		viewports[1] = new Viewport(0, 0, settings.canvas_width, settings.canvas_height);
		viewports[1].children.add(bt_back);
		viewports[1].children.add(bg_ex);
		
		bt_start = new DrawableObject(600, 600, 200, 100, images.GetImage("start"));
		DrawableObject bg_naming = new DrawableObject(0, 0, settings.canvas_width, settings.canvas_height, images.GetImage("bg_naming"));
		viewports[2] = new Viewport(0, 0, settings.canvas_width, settings.canvas_height);
		viewports[2].children.add(bt_start);
		viewports[2].children.add(bt_back);
		viewports[2].children.add(bg_naming);
		
		DrawableObject bg_chk = new DrawableObject(0, 0, settings.canvas_width, settings.canvas_height, images.GetImage("bg_chk"));
		viewports[3] = new Viewport(0, 0, settings.canvas_width, settings.canvas_height);
		viewports[3].children.add(bt_back);
		viewports[3].children.add(bg_chk);
		
		DrawableObject bg_end = new DrawableObject(0, 0, settings.canvas_width, settings.canvas_height, images.GetImage("bg_end"));
		viewports[4] = new Viewport(0, 0, settings.canvas_width, settings.canvas_height);
		viewports[4].children.add(bg_end);
		
		DrawableObject bg_game = new DrawableObject(0, 0, settings.canvas_width, settings.canvas_height, images.GetImage("bg_game"));
		viewports[5] = new Viewport(0, 0, settings.canvas_width, settings.canvas_height);
		viewports[5].children.add(bg_game);
		
		return true;
	}

	
	@Override
	public boolean Update(long timeStamp) {
		//입력을 버튼에 반영. 이 메서드는 항상 Update()의 시작 부분에서 호출해 주어야 함
		inputs.AcceptInputs();
			
		switch ( state ) {
		case Base:
			if( inputs.buttons[5].IsReleasedNow() == true ) {
				if(inputs.pos_mouseCursor.getX()>=250&&inputs.pos_mouseCursor.getX()<=450&&inputs.pos_mouseCursor.getY()>=600&&inputs.pos_mouseCursor.getY()<=700) {
					state = GameState.Naming;
				}
				
				if(inputs.pos_mouseCursor.getX()>=600&&inputs.pos_mouseCursor.getX()<=800&&inputs.pos_mouseCursor.getY()>=600&&inputs.pos_mouseCursor.getY()<=700) {
					state = GameState.Gamerole;
				}
				
				if(inputs.pos_mouseCursor.getX()>=950&&inputs.pos_mouseCursor.getX()<=1150&&inputs.pos_mouseCursor.getY()>=600&&inputs.pos_mouseCursor.getY()<=700) {
					state = GameState.CheckRanking;
				}
			}
			break;
		case Gamerole:
		case CheckRanking:
			if( inputs.buttons[5].IsReleasedNow() == true ) {
		
				if(inputs.pos_mouseCursor.getX()>=1050&&inputs.pos_mouseCursor.getX()<=1250&&inputs.pos_mouseCursor.getY()>=600&&inputs.pos_mouseCursor.getY()<=700) {
					state = GameState.Base;
				}
			}
			break;
		case Naming:
			if( inputs.buttons[5].IsReleasedNow() == true ) {
				if(inputs.pos_mouseCursor.getX()>=600&&inputs.pos_mouseCursor.getX()<=800&&inputs.pos_mouseCursor.getY()>=600&&inputs.pos_mouseCursor.getY()<=700) {
					state = GameState.Ready;
					gametime = new time();
				}
				if(inputs.pos_mouseCursor.getX()>=1050&&inputs.pos_mouseCursor.getX()<=1250&&inputs.pos_mouseCursor.getY()>=600&&inputs.pos_mouseCursor.getY()<=700) {
					state = GameState.Base;
				}
			}
			break;
		case Ready:
				//state = GameState.Running;
				timer = new Timer();
				timer.scheduleAtFixedRate(timecheck, 3000, 1000);
				timer.scheduleAtFixedRate(makecouple1, 3000, 1000);
				timer.scheduleAtFixedRate(makecouple2, 23500, 1000);
				timer.scheduleAtFixedRate(makecouple3, 53300, 1000);
				state = GameState.Running;
			break;
		case Running:
			
			p1.state = PlayerState.Stop;
			p2.state = PlayerState.Stop;

			//timer.scheduleAtFixedRate(falling, 1000, 1000);
			for(int i=couplestart;i<coupleend;i++) {
				if(couples[i]!=null) {
					if(couples[i].x>=600||couples[i].x<=100) {
						couples[i].direction*=-1;
					}
					couples[i].x += couples[i].direction*3;
					couples[i].y += 5;
					
					if(!(p1.x > couples[i].x + couples[i].width || p1.x + p1.width < couples[i].x || p1.y > couples[i].y + couples[i].height || p1.y + p1.height < couples[i].y)) {
						p1.score+=couples[i].score;
						couples[i]=null;
						couplestart++;
					}
					
					if(couples[i]!=null&&couples[i].y > 700) {
						couples[i]=null;
						couplestart++;
					}
				}
			}

			if( gametime.remaintime == 0 ) {
				timer.cancel();
				state = GameState.Finished;
			}
				
			if ( inputs.buttons[0].isPressed == true ) {
				if( p1.x>100 ) p1.x -= 8;
				p1.state = PlayerState.Left;
			}
			if ( inputs.buttons[1].isPressed == true ) {
				if( p1.x<600 ) p1.x += 8;
				p1.state = PlayerState.Right;
			}
			if ( inputs.buttons[0].isPressed == false && inputs.buttons[1].isPressed == false ) {
				p1.state = PlayerState.Stop;
			}
			
			if ( inputs.buttons[2].isPressed == true ) {
				if( p2.x>800 ) p2.x -= 8;
				p2.state = PlayerState.Left;
			}
			if ( inputs.buttons[3].isPressed == true ) {
				if( p2.x<1300 ) p2.x += 8;
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
				state = GameState.Ending;
			}
			break;
		case Ending:
			state = GameState.Base;
			break;
		}
		switch ( p1.state )	{
		case Stop:
			p1.image = images.GetImage("player");
			break;
		case Left:
			p1.image = images.GetImage("player");
			break;
		case Right:
			p1.image = images.GetImage("player");
			break;
		}
		switch ( p2.state ) {
		case Stop:
			p2.image = images.GetImage("player");
			break;
		case Left:
			p2.image = images.GetImage("player");
			break;
		case Right:
			p2.image = images.GetImage("player");
			break;
		}
				
		return true;
	}

	@Override
	public void Draw(long timeStamp) {
		//그리기 작업 시작 - 이 메서드는 Draw()의 가장 위에서 항상 호출해 주어야 함
		BeginDraw();
			
		//화면을 다시 배경색으로 채움
		ClearScreen();
		
		switch ( state )
		{
		case Base:
			viewports[0].Draw(g);

			break;
		case Gamerole:
			viewports[1].Draw(g);

			break;
		case Naming:
			viewports[2].Draw(g);

			break;
		case CheckRanking:
			viewports[3].Draw(g);

			break;
		case Started:
			break;
		case Ready:
			break;
		case Running:
			DrawString(10, 50, "스코어 %d               ", p1.score);
			for(int i=couplestart;i<coupleend;i++) {
				if(couples[i]!=null) couples[i].Draw(g);
			}
			p1.Draw(g);
			p2.Draw(g);
			gametime.Draw(g);
			break;
		case Finished:
			if ( numberOfWinner == 3 )
				DrawString(10, 50, "                둘 다 졌다.               ");
			else
				DrawString(10, 50, "               %dP가 이겼다.               ", numberOfWinner);
			
			DrawString(10, 72, "     스페이스 바를 누르면 다시 시작한다.   ");
			break;
		}
		//System.out.printf("%s\n",state);
		
		//그리기 작업 끝 - 이 메서드는 Draw()의 가장 아래에서 항상 호출해 주어야 함
		EndDraw();
	}
}
