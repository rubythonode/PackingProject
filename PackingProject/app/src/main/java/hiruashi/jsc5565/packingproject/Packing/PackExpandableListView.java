package hiruashi.jsc5565.packingproject.Packing;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import hiruashi.jsc5565.packingproject.R;
import hiruashi.jsc5565.packingproject.util.AnimationUtil;
import hiruashi.jsc5565.packingproject.util.ExpandableGroup;
import hiruashi.jsc5565.packingproject.util.PackListItem;
import hiruashi.jsc5565.packingproject.util.ViewUtil;

import static hiruashi.jsc5565.packingproject.util.AnimationUtil.ADD;
import static hiruashi.jsc5565.packingproject.util.AnimationUtil.REMOVE;

/**
 * Created by 정수찬 (jung suchan) on 2016-11-14.
 */

public class PackExpandableListView<T> extends ExpandableListView {


    //================================================
    //                  variables
    //================================================


    private Context context;
    private PackExpandableAdpater adapter;
    private int Group_Layout, Child_Layout;







    //================================================
    //                 constructor
    //================================================


    public PackExpandableListView(Context context) {
        super(context);
        Init(context, 0, 0);
    }

    public PackExpandableListView(Context context, int Group_Layout, int Child_Layout) {
        super(context);
        Init(context, Group_Layout, Child_Layout);
    }

    public PackExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context, 0, 0);
    }

    public PackExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context, 0, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PackExpandableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Init(context, 0, 0);
    }








    //================================================
    //                    init
    //================================================


    /**
     * initialize
     * @param context
     * @param Group_Layout
     * @param Child_Layout
     */
    private void Init(Context context, int Group_Layout, int Child_Layout){
        this.context = context;
        this.Group_Layout = Group_Layout;
        this.Child_Layout = Child_Layout;

        adapter = new PackExpandableAdpater(context, this.Group_Layout, this.Child_Layout);
        this.setAdapter(adapter);

    }







    //================================================
    //                base setting
    //================================================


    /**
     * set group layout
     * @param Group_Layout
     */
    public void setGroupLayout(int Group_Layout){
        adapter.setGroupLayout(Group_Layout);
    }


    /**
     * set child layout
     * @param Child_Layout
     */
    public void setChildLayout(int Child_Layout){
        adapter.setChildayout(Child_Layout);
    }


    /**
     * set group matching both id and view.
     * @param id
     * @param v
     */
    public void setGroupIdMatch(int id, int v){
        adapter.setGroupIdMatch(id, v);
    }


    /**
     * set child matching both id and view.
     * @param id
     * @param v
     */
    public void setChildIdMatch(int id, int v){
        adapter.setChildIdMatch(id, v);
    }





    //================================================
    //             control items
    //================================================


    /**
     * add group
     * @param data
     */
    public void addGroupItem(int index, T...data){
        ExpandableGroup group = new ExpandableGroup(data);
        adapter.addGroupItem(index, group);
    }


    /**
     * add child in group
     * @param GroupPosition
     * @param data
     */
    public void addChildItem(int GroupPosition, int index, T...data){
        PackListItem child = new PackListItem(data);
        adapter.addChildItem(GroupPosition, index, child);
    }


    /**
     * remove group item
     * @param GroupPosition
     */
    public void removeGroupItem(int GroupPosition){
        adapter.removeGroupItem(GroupPosition);
    }


    /**
     * remove child item in group
     * @param GroupPosition
     * @param ChildPosition
     */
    public void removeChildItem(int GroupPosition, int ChildPosition){
        adapter.removeChildItem(GroupPosition, ChildPosition);
    }








    //================================================
    //                  get item count
    //================================================

    /**
     * get group count
     * @return
     */
    public int getGroupCount(){
        return adapter.GroupData.size();
    }


    /**
     * get child count
     * @param position
     * @return
     */
    public int getChildCount(int position){
        return adapter.GroupData.get(position).getChildList().size();
    }










    /******************************************************************
     *                  Pack Expandable adapter
     ******************************************************************/


    class PackExpandableAdpater extends BaseExpandableListAdapter{






        //================================================
        //                    variables
        //================================================

        private Context context;
        private LayoutInflater inflater;
        private ArrayList<ExpandableGroup> GroupData;
        private ArrayList<Integer> Group_Layout_Id, Group_View_Order;
        private ArrayList<Integer> Child_Layout_Id, Child_View_Order;
        private int Group_Layout, Child_Layout;
        private int childVisibleCount=0;
        private int position=0;




        //================================================
        //                 constructor
        //================================================

        /**
         * contstructor
         * @param context
         */
        PackExpandableAdpater(Context context, int Group_Layout, int Child_Layout){
            this.context = context;
            this.inflater = LayoutInflater.from(this.context);

            this.GroupData = new ArrayList<>();

            this.Group_Layout = Group_Layout;
            this.Child_Layout = Child_Layout;

            Group_Layout_Id = new ArrayList<Integer>();
            Group_View_Order = new ArrayList<Integer>();

            Child_Layout_Id = new ArrayList<Integer>();
            Child_View_Order = new ArrayList<Integer>();

        }



        //================================================
        //              adapter base setting
        //================================================

        /**
         * get parent getcount
         * @return
         */
        @Override
        public int getGroupCount() {
            return GroupData.size();
        }

        /**
         * get child getcount
         * @param groupPosition
         * @return
         */
        @Override
        public int getChildrenCount(int groupPosition) {
            return GroupData.get(groupPosition).getChildList().size();
        }

        /**
         * get a group object
         * @param groupPosition
         * @return
         */
        @Override
        public Object getGroup(int groupPosition) {
            return GroupData.get(groupPosition);
        }

        /**
         * get a child object
         * @param groupPosition
         * @param childPosition
         * @return
         */
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return GroupData.get(groupPosition).getChildList().get(childPosition);
        }

        /**
         * get a group position
         * @param groupPosition
         * @return
         */
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        /**
         * get a child postion in group
         * @param groupPosition
         * @param childPosition
         * @return
         */
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        /**
         *
         * @return
         */
        @Override
        public boolean hasStableIds() {
            return true;
        }




        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }





        //================================================
        //                  getview
        //================================================

        /**
         * group view
         * @param groupPosition
         * @param isExpanded
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            View view = ViewUtil.getInstance().getView(this.context, groupPosition, convertView, parent, Group_Layout, inflater, GroupData, Group_Layout_Id, Group_View_Order);

            return view;
        }






        /**
         * child view
         * @param groupPosition
         * @param childPosition
         * @param isLastChild
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            View view = ViewUtil.getInstance().getView(this.context, childPosition, convertView, parent, Child_Layout, inflater, GroupData.get(groupPosition).getChildList(), Child_Layout_Id, Child_View_Order);

            Animation animation = AnimationUtils.loadAnimation(this.context, R.anim.over_show);
            startAnimation(animation);
            return view;
        }






        //================================================
        //           base setting in listview
        //================================================


        /**
         * set group matching both id and view.
         * @param id
         * @param order
         */
        public void setGroupIdMatch(int id, int order){
            if(Group_Layout_Id == null){
                Group_Layout_Id = new ArrayList<Integer>();
            }
            if(Group_View_Order == null){
                Group_View_Order = new ArrayList<Integer>();
            }
            Group_Layout_Id.add(id);
            Group_View_Order.add(order);
        }


        /**
         * set child matching both id and view.
         * @param id
         * @param order
         */
        public void setChildIdMatch(int id, int order){
            if(Child_Layout_Id == null){
                Child_Layout_Id = new ArrayList<Integer>();
            }
            if(Child_View_Order == null){
                Child_View_Order = new ArrayList<Integer>();
            }

            Child_Layout_Id.add(id);
            Child_View_Order.add(order);
        }

        /**
         * set group layout
         * @param Group_Layout
         */
        public void setGroupLayout(int Group_Layout){
            this.Group_Layout = Group_Layout;
        }


        /**
         * set child layout
         * @param Child_Layout
         */
        public void setChildayout(int Child_Layout){
            this.Child_Layout = Child_Layout;
        }





        //================================================
        //                control items
        //================================================

        /**
         * add group item
         * @param group
         */
        public void addGroupItem(int index, ExpandableGroup group){
            GroupData.add(index, group);
        }


        /**
         * add child item
         * @param groupPosition
         * @param child
         */
        public void addChildItem(int groupPosition, int index, PackListItem child){
            GroupData.get(groupPosition).addChildItem(index, child);
        }


        /**
         * remove group item
         * @param GroupPosition
         */
        public void removeGroupItem(int GroupPosition){
            GroupData.remove(GroupPosition); //remove item
        }


        /**
         * remove child item in group
         * @param GroupPosition
         * @param ChildPosition
         */
        public void removeChildItem(final int GroupPosition, final int ChildPosition){
            GroupData.get(GroupPosition).getChildList().remove(ChildPosition);
        }




        /**
         *
         * @param groupPosition
         */

        @Override
        public void onGroupExpanded(int groupPosition) {
            super.onGroupExpanded(groupPosition);
            childVisibleCount+=getChildrenCount(groupPosition);
        }

        @Override
        public void onGroupCollapsed(int groupPosition) {
            super.onGroupCollapsed(groupPosition);
            childVisibleCount-=getChildrenCount(groupPosition);
        }
    }
}