using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace Kgsh_meal_Xamarin
{
    class MealViewHolder
    {
        public TextView morning; // 아침 점심 저녁
        public TextView data1; // 밥
        public TextView data2; // 국
        public TextView data3; // 반찬들
        public TextView data4; //
        public TextView data5; //
        public TextView data6; //

        public FrameLayout choiceLayout;

        public ImageButton good;
        public ImageButton bad;

        public int time;

        public SeekBar choiceBar;

        MealViewHolder(TextView toMorning)
        {
            morning = toMorning;
        }
    }
}