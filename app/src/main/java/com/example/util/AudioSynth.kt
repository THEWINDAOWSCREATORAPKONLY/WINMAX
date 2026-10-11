package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.example.model.OsFamily
import kotlin.concurrent.thread
import kotlin.math.sin

object AudioSynth {

    fun playBeep(frequency: Float = 440f, durationMs: Int = 120, volume: Float = 0.5f) {
        thread(start = true, isDaemon = true) {
            try {
                val sampleRate = 22050
                val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
                val generatedSnd = ShortArray(numSamples)
                for (i in 0 until numSamples) {
                    val angle = 2.0 * Math.PI * i / (sampleRate / frequency)
                    generatedSnd[i] = (sin(angle) * Short.MAX_VALUE * volume).toInt().toShort()
                }

                val bufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                ).coerceAtLeast(numSamples * 2)

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(generatedSnd, 0, numSamples)
                audioTrack.play()
                Thread.sleep(durationMs.toLong() + 20)
                audioTrack.stop()
                audioTrack.release()
            } catch (e: Exception) {
            }
        }
    }

    fun playBootChime(osFamily: OsFamily) {
        thread(start = true, isDaemon = true) {
            try {
                when (osFamily) {
                    OsFamily.WINDOWS -> {
                        // Classic Windows major chord arpeggio (Eb, Bb, Eb, G, Bb)
                        val notes = listOf(311.13f, 466.16f, 622.25f, 783.99f, 932.33f)
                        for (note in notes) {
                            playBeep(note, 160, 0.4f)
                            Thread.sleep(130)
                        }
                    }
                    OsFamily.MACOS -> {
                        // Iconic Macintosh Startup Chime (Rich F# Major chord: F#3, A#3, C#4, F#4)
                        val chordNotes = listOf(185.0f, 233.08f, 277.18f, 369.99f, 554.37f)
                        for (note in chordNotes) {
                            playBeep(note, 450, 0.35f)
                            Thread.sleep(30)
                        }
                    }
                    OsFamily.LINUX -> {
                        // Linux marimba boot chord (F, A, C, E)
                        val notes = listOf(349.23f, 440.00f, 523.25f, 659.25f)
                        for (note in notes) {
                            playBeep(note, 140, 0.35f)
                            Thread.sleep(110)
                        }
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    fun playErrorChord() {
        thread(start = true, isDaemon = true) {
            playBeep(220f, 180, 0.6f)
            Thread.sleep(50)
            playBeep(180f, 250, 0.7f)
        }
    }

    fun playClickSound() {
        playBeep(880f, 25, 0.2f)
    }

    fun playSuccessFanfare() {
        thread(start = true, isDaemon = true) {
            val notes = listOf(523.25f, 659.25f, 783.99f, 1046.50f)
            for (note in notes) {
                playBeep(note, 120, 0.4f)
                Thread.sleep(90)
            }
        }
    }

    fun vibrate(context: Context, durationMs: Long = 40) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            vibrator?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    it.vibrate(durationMs)
                }
            }
        } catch (e: Exception) {
        }
    }
}
