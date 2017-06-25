package yan.yxs.music.ThreadUtils;

import android.annotation.SuppressLint;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
	
	private static ThreadPool utils = new ThreadPool();
	private ThreadPoolProx poolProxy;
	public static ThreadPool getInstence (){
		return utils;
	}
	public ThreadPoolProx createPool(){
		if(poolProxy == null){
			poolProxy = new ThreadPoolProx(3,3);
		}
		return poolProxy;
	}
	
	public class ThreadPoolProx{
		private ThreadPoolExecutor pool;
		private int corePoolSize ;
		private int maximumPoolSiza;
		public ThreadPoolProx(int corePoolSize, int maximumPoolSiza) {
			this.corePoolSize = corePoolSize;
			this.maximumPoolSiza = maximumPoolSiza;
		}
		@SuppressLint("NewApi")
		public void execute(Runnable runnable){
			if(pool == null){
				/*
				 * �����̳߳�
				 * 1�̳߳����������ٸ��߳�
				 * 2����Ŷ����ˣ����⿪���߳���
				 * 3 ����̳߳���û��Ҫִ�е����� �����
				 * 4���ʱ�䵥λ
				 * 5����̳߳����������̶߳��Ѿ����� ʣ�µ����� ��ʱ�浽LinkedBlockingQuenur�������Ŷ�
				 */
				pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSiza, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));
			}
			pool.execute(runnable);
		}
		
	}
}
