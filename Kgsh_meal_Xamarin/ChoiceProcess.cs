using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace Kgsh_meal_Xamarin
{
    class ChoiceProcess:Thread
    {
        private boolean _good;
        private int _time;
        private SeekBar _choice;

        bool onlyChoice = false;

        public Handler ChoiceHandler = new Handler()
        {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    {
                        Toast.MakeText(MealMainActivity.instance.getContext(), "오늘 식단에  투표해주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    {
                        Toast.MakeText(MealMainActivity.instance.getContext(), "오늘 이 식단에는 이미 투표하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    {
                        Toast.MakeText(MealMainActivity.instance.getContext(), "해당 날짜에만 투표해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

}
