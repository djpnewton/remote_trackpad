using System;
using System.Collections.Generic;
using System.Text;
using System.Runtime.InteropServices;

namespace InputController
{
    public static class SoundVolume
    {
        const int VolumeSteps = 15;

        public static void VolumeUp()
        {
            if (System.Environment.OSVersion.Version.Major >= 6)
            {
                //
                // Use new vista volume apis
                //

                EndpointVolume epVol = new EndpointVolume();
                float vol = epVol.MasterVolume;
                epVol.MasterVolume = Math.Min(1, vol += 1.0f / VolumeSteps);
                epVol.Dispose();
            }
            else
            {
                //
                // Use older volume apis on XP
                //

                WinMMVolume.MixerInfo mi = WinMMVolume.GetMixerControls();
                WinMMVolume.AdjustVolume(mi, (mi.maxVolume - mi.minVolume) / VolumeSteps);
            }
        }

        public static void VolumeDown()
        {
            if (System.Environment.OSVersion.Version.Major >= 6)
            {
                //
                // Use new vista volume apis
                //

                EndpointVolume epVol = new EndpointVolume();
                float vol = epVol.MasterVolume;
                epVol.MasterVolume = Math.Max(0, vol -= 1.0f / VolumeSteps);
                epVol.Dispose();
            }
            else
            {
                //
                // Use older volume apis on XP
                //

                WinMMVolume.MixerInfo mi = WinMMVolume.GetMixerControls();
                WinMMVolume.AdjustVolume(mi, -(mi.maxVolume - mi.minVolume) / VolumeSteps);
            }
        }
    }
}
