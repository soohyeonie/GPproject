import java.awt.Color;
import java.awt.event.MouseEvent;

import loot.GameFrame;
import loot.GameFrameSettings;

/**
 * LOOT�� ���� ��� ����� ���������� Ȱ���� �����Դϴ�.
 * 
 * @author Racin
 *
 */
@SuppressWarnings("serial")
public class AudioSampleFrame extends GameFrame
{
	/*
	 * ����:
	 * 
	 * ���콺 ���� ��ư:
	 * 
	 * 		������ �ִ� ���� ���� ��� ����
	 * 		- �ּ� ���ݿ� �°� ������ ����� �õ�
	 * 		- ��� ������ ������ ä�� �� ������ �°� ���� �� ��ø�Ǿ� �鸮�� ��
	 * 
	 */
	
	/* -------------------------------------------
	 * 
	 * ��� ���� �κ�
	 * 
	 */
	
	//�� Play() ȣ�� ������ �ּ� ������ �����մϴ�. (���� ������ ������ ���࿡ ���� ������ �߻��� �� �ֽ��ϴ�)
	static final long interval_play_ms = 600;
	
	//����� ä�� ��('���ÿ�' �󸶳� ���� ����� �� �ִ���)�� �����մϴ�. 
	static final int number_of_channels = 5;
	
	
	/* -------------------------------------------
	 * 
	 * �ʵ� ���� �κ�
	 * 
	 */
	
	//ȣ�� ������ ��� ���� ���������� ����� �����ߴ� �������� ���� �ð��� ��� �δ� �ʵ�
	long timeStamp_lastPlayed = 0;

	
	/* -------------------------------------------
	 * 
	 * �޼��� ���� �κ�
	 * 
	 */
	
	public AudioSampleFrame(GameFrameSettings settings)
	{
		super(settings);
		
		inputs.BindMouseButton(MouseEvent.BUTTON1, 0);

		audios.LoadAudio("Audios/sample.wav", "sample", number_of_channels);
	}

	@Override
	public boolean Initialize()
	{
		//�� ������ ��� �ʱ�ȭ�� ������ ����
		return true;
	}

	@Override
	public boolean Update(long timeStamp)
	{
		//�Է��� ��ư�� �ݿ�. �� �޼���� �׻� Update()�� ���� �κп��� ȣ���� �־�� ��
		inputs.AcceptInputs();
		
		//��ư�� ������ ������ ���������� ����� �������� ���� �ð� �̻� ����� ���
		if ( inputs.buttons[0].isPressed == true && timeStamp - timeStamp_lastPlayed > interval_play_ms )
		{
			//���� �ð��� ����� �ΰ� ��� ����
			timeStamp_lastPlayed = timeStamp;
			audios.Play("sample");
		}
		
		return true;
	}

	@Override
	public void Draw(long timeStamp)
	{
		//�׸��� �۾� ���� - �� �޼���� Draw()�� ���� ������ �׻� ȣ���� �־�� ��
		BeginDraw();

		//��ư�� ������ �ִٸ� ����� û�갡�������� ĥ��
		if ( inputs.buttons[0].isPressed == true )
		{
			SetColor(Color.cyan);
			g.fillRect(0, 0, settings.canvas_width, settings.canvas_height);
			SetColor(Color.black);
			DrawString(30, 34, "���⸦ ����������");
		}
		//�׷��� ���� ��� ����� �⺻ �������� ĥ��
		else
		{
			ClearScreen();
			DrawString(30, 34, "���⸦ ����������");
		}
		
		//�׸��� �۾� �� - �� �޼���� Draw()�� ���� �Ʒ����� �׻� ȣ���� �־�� ��
		EndDraw();
	}

}
