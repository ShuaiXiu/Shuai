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
				 * 创建线程池
				 * 1线程池里面管理多少个线程
				 * 2如果排队满了，额外开的线程数
				 * 3 如果线程池里没有要执行的任务 存活多久
				 * 4存活时间单位
				 * 5如果线程池里面管理的线程都已经用了 剩下的任务 临时存到LinkedBlockingQuenur对象中排队
				 */
				pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSiza, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));
			}
			pool.execute(runnable);
		}
		
	}
}
