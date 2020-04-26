package com.hawksjamesf.mockserver

import android.app.AlarmManager
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import com.orhanobut.logger.Logger

/**
 * Copyright ® $ 2017
 * All right reserved.
 *
 * @author: hawks.jamesf
 * @since: Dec/28/2019  Sat
 */
class MockJobService : JobService() {
    companion object {
        private const val TAG = Constants.TAG + "/MockJobService"
        @JvmStatic
        fun startService(activity: Context) {
            val jobInfo = JobInfo.Builder(0, ComponentName(activity, MockJobService::class.java))
                    .setPersisted(true) //设备重启后是否继续执行
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) // 需要满足网络条件，默认值NETWORK_TYPE_NONE
//                .setMinimumLatency(5000)// 任务最少延迟时间为5s
//                .setOverrideDeadline(60000)// 任务deadline，当到期没达到指定条件也会开始执行
                    .setPeriodic(AlarmManager.INTERVAL_FIFTEEN_MINUTES) //循环执行，循环时长为一天（最小为15分钟）
//                .setRequiresCharging(true)// 需要满足充电状态
//                .setRequiresDeviceIdle(false)// 设备处于Idle(Doze)
//                .setBackoffCriteria(3000,JobInfo.BACKOFF_POLICY_LINEAR) //设置退避/重试策略
                    .build()
            MockManager.getJobScheduler().schedule(jobInfo)
        }
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Logger.t(TAG).d("onStartJob")
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Logger.t(TAG).d("onStopJob")
        return true
    }


}