package com.foodorder.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.foodorder.beans.AppConstants;
import com.foodorder.beans.ApplicationData;
import com.foodorder.beans.FoodListsViewImage;
import com.foodorder.beans.MenuModel;
import com.foodorder.client.R;
import com.foodorder.view.MenuListActivity.MyBaseAdapter;

public class ShoppingCartActivity extends Activity {
	
	private ListView cartView;
	private ArrayList<MenuModel> menuList;
	private ListView listView;
	private MyBaseAdapter myBaseAdapter;
	static String path = AppConstants.path;
	private Intent intentViewCart;
	private Intent intentConfirmPage;
	private Intent intentBack;
	private Bundle b;
	private Button btnOrderConfirm;
	private Button btnBack;
	private Button btnUpdate;
	private ArrayList<HashMap<String, String>> currentOrderline;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);          
        setTitle("Shopping Cart");		
        
        intentViewCart = getIntent();
		b = intentViewCart.getExtras();
		b.get("ViewCart");         

		
		this.btnOrderConfirm = (Button) findViewById(R.id.btnOrderConfirm);	
		this.btnOrderConfirm.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {  		
			intentConfirmPage = new Intent(ShoppingCartActivity.this,OrderConfirmActivity.class);
			intentConfirmPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intentConfirmPage.putExtra("OrderConfirm","Confirm order page loaded successfully");
			startActivity(intentConfirmPage);	

		}

	});
		

		listView = (ListView) findViewById(R.id.listView1);
		menuList = ApplicationData.getCartList();	
		currentOrderline = ApplicationData.getOrderLine();
		myBaseAdapter = new MyBaseAdapter();
		listView.setAdapter(myBaseAdapter);	
		

		//this.btnUpdate = (Button) findViewById(R.id.btnUpdate);	
		//this.btnUpdate.setOnClickListener(new OnClickListener() {
		//public void onClick(View v) {			
		//}
		//});
		
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shopping_cart, menu);
		return true;
	}
	
	class MyBaseAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return menuList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (menuList == null) {
				return convertView;
			}
			final View view = convertView.inflate(ShoppingCartActivity.this, R.layout.subcate_listview_cart, null);
			Object obj = menuList.get(position);
			ImageView menu_item_image = (ImageView) view.findViewById(R.id.img);
			TextView menu_item_title = (TextView) view.findViewById(R.id.info);
			Button right_flag = (Button) view.findViewById(R.id.btnRemove);
			final EditText txtQuantity = (EditText) view.findViewById(R.id.txtQty1);
			if ( txtQuantity != null )
		     {								
				HashMap<String, String> orderObject = currentOrderline.get(position);
				Set<String> keys = orderObject.keySet();
				String itemId = null;
				for(String key: keys){
				itemId= key;
				}
				txtQuantity.setText(orderObject.get(itemId));	
				
		     }
			
				
			
			right_flag.setTag(0);
			
			right_flag.setOnClickListener(new AdapterView.OnClickListener() {
				public void onClick(View v) {  
								
					int position = (Integer)v.getTag();
					Toast.makeText(getApplicationContext(), "Array position number " + position, Toast.LENGTH_LONG).show();		
					
					 AlertDialog.Builder adb=new AlertDialog.Builder(ShoppingCartActivity.this);
				        adb.setTitle("Delete?");
				        adb.setMessage("Are you sure you want to delete this item?");
				        final int positionToRemove = position;
				        adb.setNegativeButton("Cancel", null);
				        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
				            public void onClick(DialogInterface dialog, int which) {
				            	ArrayList<MenuModel> listMenuApp = ApplicationData.getCartList();
								MenuModel aMenu = menuList.get(positionToRemove);
								listMenuApp.remove(aMenu);
								ApplicationData.setCartList(listMenuApp);
								
								ArrayList<HashMap<String, String>> currentOrderline = ApplicationData.getOrderLine();
								HashMap<String, String> orderObject = currentOrderline.get(positionToRemove);
								currentOrderline.remove(orderObject);
								ApplicationData.setOrderLineList(currentOrderline);	
								
				                notifyDataSetChanged();
				            }});
				        adb.show();
				
				}

			});
			
			if (obj instanceof MenuModel) {
				final MenuModel aMenuItem = (MenuModel) obj;
				menu_item_title.setText("Name: " + aMenuItem.getName() + "\n"
						+ "Description: " + aMenuItem.getDes());

				menu_item_image.setTag(aMenuItem.getPic());
				if (menuList.get(position).getPic() != null
						&& !menuList.get(position).getPic().equals("")) {
					try {
						new FoodListsViewImage(ShoppingCartActivity.this)
						.loadingImage(menuList.get(position).getPic(),
								menu_item_image, R.drawable.computer, listView);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					menu_item_image.setImageResource(R.drawable.computer);
				}
				
			} 
			else {
			}
			return view;
		}
	}

	/*public static Bitmap getPicByPath(String picName) {
		picName = picName.substring(picName.lastIndexOf("/") + 1);
		String filePath = path + picName;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath);
		return bitmap;
	
	}	*/
}
