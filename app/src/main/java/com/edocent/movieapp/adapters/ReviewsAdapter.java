package com.edocent.movieapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edocent.movieapp.R;
import com.edocent.movieapp.model.Review;
import com.edocent.movieapp.utilities.AppConstants;

import java.util.List;

/**
 * Created by SRIVASTAVAA on 10/20/2015.
 */
public class ReviewsAdapter extends ArrayAdapter<Review> {

    static final String TAG = ReviewsAdapter.class.getSimpleName();

    Context mContext;
    int resource;
    List<Review> mReviewList;

    public ReviewsAdapter(Context context, int resource, List<Review> mReviewList) {
        super(context, resource, mReviewList);
        this.mContext = context;
        this.resource = resource;
        this.mReviewList = mReviewList;
    }


    /*
    * Sample response
    * {
    "id": 177677,
    "page": 1,
    "results": [
        {
            "id": "55da46c8c3a36831f6005a5e",
            "author": "leros",
            "content": "good!",
            "url": "http://j.mp/1KeCis6"
        },
        {
            "id": "56207f7092514171d4006ad6",
            "author": "Frank Ochieng",
            "content": "Some over-stuffed blockbuster actioners boldly boast their explosive and invigorating productions with dynamic glee. Well, the ‘Mission: Impossible’ film franchise echoes this same sentiment, with flashy fifty-something star Tom Cruise still carrying that boyish exuberance that never seems to miss a beat, and can breathe a sigh of relief because the latest chapter will not disappoint in its adventurous, adrenaline-rushing skin. Yes, Cruise is back as IMF super spy stud Ethan Hunt in writer-director Christopher McQuarrie’s power-surging ‘Mission: Impossible-Rogue Nation’. Notoriously slick, ambitious, wildly impish and vastly intriguing, ‘Rogue Nation’ incorporates superb direction, writing and, of course, its no-nonsense dosage of non-stop shenanigans to fuel the audience’s escapist palates.\r\n\r\nIn actuality, ‘Rogue Nation’ is a hyper and hip throwback to the original blueprint for this kinetic movie series that took viewers by storm when it first premiered back in 1996. The minor outcry for the box office sensation that was the ‘Mission: Impossible’ big screen offering back in the late 90s was quite understandable since it did not seem to stay entirely true to the iconic 60s television series thus being dismissed as a volt-driven vanity piece for the high-strutting Cruise. Still, the cinematic ‘Mission: Impossible’ experience did not peter out but steadily built a devoted fan base that wanted to endure the high-flying hedonism of Cruise’s espionage daredevil Hunt and his band of cunning cohorts. It is definitely safe to say that the stellar ‘Mission: Impossible-Rogue Nation’ has delivered a bang for its buck in a summertime cinema hunger that thrives for such sleek, innovative spy thrillers that charm more than harm. Maybe ‘Rogue Nation’ will not let anyone forget the exploits of the legendary James Bond spy capers anytime soon but hey…Cruise’s hotshot Hunt has a colorfully distinctive way of promoting his ‘shaken, not stirred’ moments as well.\r\n\r\nSo how did the collaboration of filmmaker McQuarrie and his chance-taking lead Cruise give ‘Rogue Nation’ its deliciously sinister, overactive pulse? The fact that the overzealous stuntwork (much of it performed by Cruise with daring enthusiasm) is imaginative and the story feels smart and clever certainly has a lot to do with the magnetic appeal of ‘Rogue Nation’. Hey, the TV advertisements even give a generous peek into the pulsating platitudes that the MI universe will swallow with robust anticipation. Watch IMF agent Hunt hang on to a speedy airplane with his dear life in the balance. Watch IMF agent Hunt jump into a deadly spiral of a waterfall. Watch IMF agent Hunt race a piercingly fast motorcycle (or car…take your pick) and enjoy the road raging carnage with poetic prominence. Okay…you get the picture.\r\n\r\nThe premise in ‘Mission: Impossible-Rogue Nation’ has more bounce to it than a basketball during the NBA playoffs and that is not necessarily a bad thing to note. While Hunt continues his mission to stop the team’s latest despicable foe (Sean Harris) and end the operation of the insidious operation known as the Syndicate there are feathers being ruffled because of the federalised threats to shut down the IMF empire through the suggestion of a top-notch CIA director (Alec Baldwin). Naturally, IMF head honcho Brandt (Jeremy Renner) tries to prevent such hasty actions as his team of agents must overcome some of the controversy and confrontations from previous disastrous events that have warranted the threatening hints to shutdown his governmental outfit.\r\n\r\nCruise's Agent Ethan Hunt is leaving on a jet plane and doesn't know how long he'll be back again in the eye-popping actioner MISSION: IMPOSSIBLE-ROGUE NATION.\r\nCruise’s Agent Ethan Hunt is leaving on a jet plane and doesn’t know how long he’ll be back again in the eye-popping actioner MISSION: IMPOSSIBLE-ROGUE NATION.\r\nOne cannot say enough about the ponderous yet action-packed ‘Rogue Nation’ as this exceedingly spry and hyperactive spy caper keeps one on their anxious feet while never letting up for a gasp of air. The exotic locales, showy opera houses, heart-pounding action sequences, innovative chase scenes on wheels, over-the-top baddies, well-choreographed fist fights and, of course, Cruise’s roguish Hunt and his willingness to soak up the mischievousness and mayhem of the proceedings allows this particular ‘Mission: Impossible’ installment to resonate so soundly in its off-kilter, energetic greatness. The supporting players such as Simon Pegg’s Benji and Ving Rhames’s Luther are on hand to contribute to the landscape of the triumphant cloak-and-dagger goings-on. In particular, Rebecca Ferguson is the transfixing tart whose presence as Cruise’s enigmatic female lead is easily a scene stealer. Can she be trusted or not? Who cares? In the long run, Ferguson’s inclusion is almost mandatory just to spice up this first-rate popcorn pleaser a tad bit more. Tom Hollander (‘In the Loop’) adds some flavor in the mix as the unpredictable British Prime Minister.\r\n\r\nMcQuarrie (who worked with Cruise previously on ‘Jack Reacher’ and helmed ‘The Way Of The Gun’) had a tough act to follow in terms of trying to keep stride with prior ‘Mission: Impossible’ big names in auteurs Brian De Palma, John Woo, J.J. Abrams and Brad Bird. Indeed, that is a tall order to fill. However, as the Oscar-winner screenwriter for ‘The Usual Suspects’ McQuarrie has shown that his take on the fifth edition of the ‘Mission: Impossible’ film franchise with ‘Rogue Nation’ can easily be as defiant and defining as any of his predecessors’ intense, eye-popping outings.\r\n\r\n‘Mission: Impossible-Rogue Nation’, with its lavish set pieces and sophisticated sense of winding and grinding with the twitchy antics of Cruise leading the pack, is perhaps the closest thing to resembling the elegance and excellence of the indomitable Agent 007.\r\n\r\nMission: Impossible-Rogue Nation (2015)\r\n\r\nParamount Pictures\r\n\r\n2 hrs. 12 mins.\r\n\r\nStarring: Tom Cruise, Simon Pegg, Jeremy Renner, Ving Rhames, Sean Harris, Rebecca Ferguson, Tom Hollander, Simon McBurney and Zang Jingchu\r\n\r\nDirected and Written by: Christopher McQuarrie\r\n\r\nRating: PG-13\r\n\r\nGenre: Spy Thriller/Action-Adventure/Intrigue and Espionage\r\n\r\nCritic’s Rating: *** 1/2 stars (out of 4 stars)",
            "url": "http://j.mp/1ZHsEos"
        }
    ],
    "total_pages": 1,
    "total_results": 2
    }
    * */

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        ViewHolderItem viewHolderItem;
        if(mReviewList != null && mReviewList.size() > 0){
            Log.v(TAG, "Review list size in adapter "+mReviewList.size());
            Review review = mReviewList.get(position);
            Log.v(TAG, "Got review object "+review.getAuthor());
            if(convertView == null){
                LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
                convertView = inflater.inflate(resource, viewGroup, false);

                viewHolderItem = new ViewHolderItem();
                viewHolderItem.reviewTitle = (TextView) convertView.findViewById(R.id.reviewTitleId);
                viewHolderItem.reviewAuthor = (TextView) convertView.findViewById(R.id.reviewAuthorId);

                convertView.setTag(viewHolderItem);
            }else{
                viewHolderItem = (ViewHolderItem) convertView.getTag();
            }

            viewHolderItem.reviewTitle.setText(getReviewTitle(review.getContent()));
            viewHolderItem.reviewAuthor.setText("By "+review.getAuthor());
        }

        return convertView;
    }

    static class ViewHolderItem{
        TextView reviewTitle;
        TextView reviewAuthor;
    }

    public String getReviewTitle(String content){
        if(content != null && !content.equals("") && content.length() > AppConstants.REVIEW_TITLE_LENGTH){
            content = content.substring(0, AppConstants.REVIEW_TITLE_LENGTH);
            return content;
        }
        return content;
    }
}
