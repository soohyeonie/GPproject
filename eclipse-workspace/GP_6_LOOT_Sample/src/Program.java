import loot.GameFrame;
import loot.GameFrameSettings;


public class Program
{

	public static void main(String[] args)
	{
			GameFrameSettings settings = new GameFrameSettings();
			settings.window_title = "욕망의 항아리";
			settings.canvas_width = 1400;
			settings.canvas_height = 800;
			
			//settings.gameLoop_interval_ns = 10000000;		//100FPS에 해당 - '동시에 키를 입력'하는 상황이 상대적으로 적게 연출됨
			settings.gameLoop_interval_ns = 16666666;		//약 60FPS에 해당
			//settings.gameLoop_interval_ns = 100000000;	//10FPS에 해당 - '동시에 키를 입력'하는 상황이 꽤 자주 연출됨
			
			settings.gameLoop_use_virtualTimingMode = false;
			settings.numberOfButtons = 15;
			
			GameFrame window = new InputSampleFrame(settings);
			//GameFrame window = new SampleFrame(settings);
			window.setVisible(true);
	}
}
