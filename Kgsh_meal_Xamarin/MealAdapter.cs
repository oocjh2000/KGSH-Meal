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
    class MealAdapter : BaseAdapter
    {

        Context context;

        private List<MealListData> mealLists;

        public MealAdapter(Context context)
        {
            this.context = context;
           // this.mealLists=MainActivity
        }

        public  int getCount()
        {
            return mealLists.Count;
        }
        public override Java.Lang.Object GetItem(int position)
        {
            return position;
        }

        public override long GetItemId(int position)
        {
            return position;
        }

        public override View GetView(int position, View convertView, ViewGroup parent)
        {
            var view = convertView;
            MealAdapterViewHolder holder = null;

            if (view != null)
                holder = view.Tag as MealAdapterViewHolder;

            if (holder == null)
            {
                holder = new MealAdapterViewHolder();
                var inflater = context.GetSystemService(Context.LayoutInflaterService).JavaCast<LayoutInflater>();
                
                //holder=new mealv
                
                view.Tag = holder;
            }


            //fill in your items
            //holder.Title.Text = "new text here";

            return view;
        }

        //Fill in cound here, currently 0
        public override int Count
        {
            get
            {
                return 0;
            }
        }

    }

    class MealAdapterViewHolder : Java.Lang.Object
    {
        //Your adapter views to re-use
        //public TextView Title { get; set; }
    }
}