package ca.pluszero.emotive.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.HashSet;
import java.util.Set;

import ca.pluszero.emotive.R;
import ca.pluszero.emotive.activities.MainActivity;
import ca.pluszero.emotive.models.Choice;

public class EmotiveAppWidgetProvider extends AppWidgetProvider {

    private static final Set<String> CHOICE_ACTIONS = new HashSet();
    static {
        for (Choice choice : Choice.values()) {
            CHOICE_ACTIONS.add(choice.toString());
        }
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            PendingIntent pendingFoodIntent = getIntentWithChoice(context, Choice.FOOD);
            PendingIntent pendingListenIntent = getIntentWithChoice(context, Choice.LISTEN);
            PendingIntent pendingLearnIntent = getIntentWithChoice(context, Choice.GOOGLE);
            PendingIntent pendingFindIntent = getIntentWithChoice(context, Choice.FIND);
            PendingIntent pendingWatchIntent = getIntentWithChoice(context, Choice.YOUTUBE);
            PendingIntent pendingWeatherIntent = getIntentWithChoice(context, Choice.WEATHER);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.bFirstOption, pendingFoodIntent);
            views.setOnClickPendingIntent(R.id.imgFirstOption, pendingFoodIntent);
            views.setOnClickPendingIntent(R.id.bSecondOption, pendingListenIntent);
            views.setOnClickPendingIntent(R.id.imgSecondOption, pendingListenIntent);
            views.setOnClickPendingIntent(R.id.bThirdOption, pendingLearnIntent);
            views.setOnClickPendingIntent(R.id.imgThirdOption, pendingLearnIntent);
            views.setOnClickPendingIntent(R.id.bFourthOption, pendingFindIntent);
            views.setOnClickPendingIntent(R.id.imgFourthOption, pendingFindIntent);
            views.setOnClickPendingIntent(R.id.bFifthOption, pendingWatchIntent);
            views.setOnClickPendingIntent(R.id.imgFifthOption, pendingWatchIntent);
            views.setOnClickPendingIntent(R.id.bSixthOption, pendingWeatherIntent);
            views.setOnClickPendingIntent(R.id.imgSixthOption, pendingWeatherIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null && CHOICE_ACTIONS.contains(intent.getAction())) {
            String choiceAction = intent.getAction();
            Intent newIntent = new Intent(context, MainActivity.class);
            newIntent.putExtra(MainActivity.PRESSED_OPTION, Choice.getEnumForString(choiceAction));
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            context.startActivity(newIntent);
        }
    }

    private static PendingIntent getIntentWithChoice(Context context, Choice choice) {
        Intent intent = new Intent(context, EmotiveAppWidgetProvider.class);
        intent.setAction(choice.toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pendingIntent;
    }
}
