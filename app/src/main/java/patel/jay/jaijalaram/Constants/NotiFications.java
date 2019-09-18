package patel.jay.jaijalaram.Constants;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import patel.jay.jaijalaram.Models.Customer;
import patel.jay.jaijalaram.Models.Order;
import patel.jay.jaijalaram.Panel.ShowItem.OrderItemsActivity;
import patel.jay.jaijalaram.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotiFications {

    public static void orderNoti(Context activity, Order order, int i) {
        try {
            TimeConvert tc = TimeConvert.timeMiliesConvert(order.getCurrentTime());

            Customer cust = Customer.customer(activity, order.getCustId());

            Intent callI = new Intent(Intent.ACTION_CALL);
            callI.setData(Uri.parse("tel:" + cust.getMobile()));

            OrderItemsActivity.OITEM = null;
            Intent intent = new Intent(activity, OrderItemsActivity.class);
            intent.putExtra(MYSQL.Order.OID, order);
            intent.putExtra("isNoti", true);

            PendingIntent pIntent = PendingIntent.getActivity(activity, (int) System.currentTimeMillis(), intent, 0);

            PendingIntent callPI = PendingIntent.getActivity(activity, (int) System.currentTimeMillis(), callI, 0);

            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(activity);
            builder
                    .setContentText(order.getPrice() + " " + activity.getString(R.string.rs))
                    .setSmallIcon(R.drawable.cart)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .addAction(android.R.drawable.sym_action_call, "Call", callPI)
                    .addAction(android.R.drawable.ic_menu_more, "Open", pIntent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000})
                    .setLights(Color.RED, 3000, 3000)
                    .setSound(sound)
                    .setStyle(new NotificationCompat.MessagingStyle("")
                            .setConversationTitle("Order Placed By: " + cust.toString())
                            .addMessage(tc.getHH_Min_AP() + "", 1, "Time")
                            .addMessage(order.getPrice() + "", 2, "Price")
                    );

            Notification noti = builder.build();

            NotificationManager notiManager =
                    (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);

            notiManager.notify(i, noti);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
