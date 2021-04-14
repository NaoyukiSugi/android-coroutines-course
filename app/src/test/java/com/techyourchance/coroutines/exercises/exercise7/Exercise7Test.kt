package com.techyourchance.coroutines.exercises.exercise7

import com.techyourchance.coroutines.common.TestUtils.printJobsHierarchy
import kotlinx.coroutines.*
import org.junit.Test

class Exercise7Test {

    /*
    Write nested withContext blocks, explore the resulting Job's hierarchy, test cancellation
    of the outer scope
     */
    @Test
    fun nestedWithContext() {
        runBlocking {
            val scopeJob = Job()
            val scope = CoroutineScope(scopeJob + CoroutineName("outer scope") + Dispatchers.IO)
            scope.launch(CoroutineName("coroutine")) {
                try {
                    delay(100)
                    withContext(CoroutineName("withContext") + Dispatchers.Default) {
                        try {
                            delay(100)
                            withContext(CoroutineName("nested withContext")) {
                                try {
                                    printJobsHierarchy(scopeJob)
                                    delay(100)
                                    println("nested withContext completed")
                                } catch (e: CancellationException) {
                                    println("nested cancelled")
                                }
                            }
                            println("withContext completed")
                        } catch (e: CancellationException) {
                            println("withContext cancelled")
                        }
                    }
                    println("coroutine completed")
                } catch (e: CancellationException) {
                    println("coroutine cancelled")
                }
            }
            launch {
                delay(250)
                scope.cancel()
            }
            scopeJob.join()
            println("test done")
        }
    }

    /*
    Launch new coroutine inside another coroutine, explore the resulting Job's hierarchy, test cancellation
    of the outer scope, explore structured concurrency
     */
    @Test
    fun nestedLaunchBuilders() {
        runBlocking {
            val scopeJob = Job()
            val scope = CoroutineScope(scopeJob + CoroutineName("outer scope") + Dispatchers.IO)
            scope.launch(CoroutineName("coroutine")) {
                try {
                    delay(100)
                    withContext(CoroutineName("withContext") + Dispatchers.Default) {
                        try {
                            delay(100)
                            launch(CoroutineName("nested coroutine")) {
                                try {
                                    printJobsHierarchy(scopeJob)
                                    delay(100)
                                    println("nested coroutine completed")
                                } catch (e: CancellationException) {
                                    println("nested cancelled")
                                }
                            }
                            println("withContext completed")
                        } catch (e: CancellationException) {
                            println("withContext cancelled")
                        }
                    }
                    println("coroutine completed")
                } catch (e: CancellationException) {
                    println("coroutine cancelled")
                }
            }
            launch {
                delay(250)
                scope.cancel()
            }
            scopeJob.join()
            println("test done")
        }
    }

    /*
    Launch new coroutine on "outer scope" inside another coroutine, explore the resulting Job's hierarchy,
    test cancellation of the outer scope, explore structured concurrency
     */
    @Test
    fun nestedCoroutineInOuterScope() {
        runBlocking {
            val scopeJob = Job()
            val scope = CoroutineScope(scopeJob + CoroutineName("outer scope") + Dispatchers.IO)
            scope.launch {
                withContext(CoroutineName("withContext")) {
                    scope.launch {

                    }
                }
            }
            scopeJob.join()
            println("test done")
        }
    }

}
