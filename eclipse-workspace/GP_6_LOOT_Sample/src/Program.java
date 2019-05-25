import loot.GameFrame;
import loot.GameFrameSettings;


public class Program
{

	public static void main(String[] args)
	{
			GameFrameSettings settings = new GameFrameSettings();
			settings.window_title = "����� �׾Ƹ�";
			settings.canvas_width = 1400;
			settings.canvas_height = 800;
			
			//settings.gameLoop_interval_ns = 10000000;		//100FPS�� �ش� - '���ÿ� Ű�� �Է�'�ϴ� ��Ȳ�� ��������� ���� �����
			settings.gameLoop_interval_ns = 16666666;		//�� 60FPS�� �ش�
			//settings.gameLoop_interval_ns = 100000000;	//10FPS�� �ش� - '���ÿ� Ű�� �Է�'�ϴ� ��Ȳ�� �� ���� �����
			
			settings.gameLoop_use_virtualTimingMode = false;
			settings.numberOfButtons = 15;
			
			GameFrame window = new InputSampleFrame(settings);
			//GameFrame window = new SampleFrame(settings);
			window.setVisible(true);
	}
}
