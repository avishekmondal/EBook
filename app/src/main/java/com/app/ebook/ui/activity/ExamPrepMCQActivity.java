package com.app.ebook.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.app.ebook.R;
import com.app.ebook.databinding.ActivityExamPrepMcqBinding;
import com.app.ebook.models.mcq.MCQListRequest;
import com.app.ebook.models.mcq.MCQListResponse;
import com.app.ebook.models.mcq.ReturnData;
import com.app.ebook.network.RetroClient;
import com.app.ebook.network.RetrofitListener;
import com.app.ebook.network.UrlConstants;
import com.app.ebook.util.AppUtilities;
import com.app.ebook.util.PermissionUtility;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

import static com.app.ebook.util.AppUtilities.showSnackBar;
import static com.app.ebook.util.Constants.CHAPTER;
import static com.app.ebook.util.Constants.IS_SUBSCRIBED;
import static com.app.ebook.util.Constants.MCQ_CATEGORY;

public class ExamPrepMCQActivity extends BaseActivity implements RetrofitListener {

    private ActivityExamPrepMcqBinding binding;
    private RetroClient retroClient;

    private ArrayList<ReturnData> mcqList;
    private int mcqPosition = 0;

    private TextToSpeech textToSpeech;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    private boolean isSpeakerOn = false;
    private String toSpeak = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exam_prep_mcq);

        init();
        getQuestionList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void init() {
        retroClient = new RetroClient(this, this);

        binding.textViewTitle.setText(mSessionManager.getSession(CHAPTER));

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.getDefault());
                    textToSpeech.setPitch(1.0f);
                    textToSpeech.setSpeechRate(0.75f);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        binding.buttonTextToSpeech.setVisibility(View.GONE);
                    }

                    textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            Log.v("TextToSpeech", "On Start");
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            Log.v("TextToSpeech", "On Done");
                            runOnUiThread(() -> {
                                speechRecognizer.startListening(speechRecognizerIntent);
                            });
                        }

                        @Override
                        public void onError(String utteranceId) {
                            Log.v("TextToSpeech", "On Error");
                        }
                    });
                } else {
                    binding.buttonTextToSpeech.setVisibility(View.GONE);
                }
            }
        });

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                Log.v("aaText", "Listening");
            }

            @Override
            public void onRmsChanged(float v) {
                Log.v("aa", "onRmsChanged");
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
                Log.v("aa", "onBufferReceived");
            }

            @Override
            public void onEndOfSpeech() {
                Log.v("aa", "onEndOfSpeech");
            }

            @Override
            public void onError(int i) {
                Log.v("aa", "onError");
                textToSpeech.speak("Wrong Input. Please say between A, B, C, D or Next", TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
            }

            @Override
            public void onResults(Bundle bundle) {
                Log.v("aa", "onResults");
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Log.v("aa", data.toString());

                if (data.size() > 0) {
                    switch (data.get(0).toLowerCase()) {
                        case "a":
                            binding.radioButtonOption1.setChecked(true);
                            break;
                        case "b":
                            binding.radioButtonOption2.setChecked(true);
                            break;
                        case "c":
                            binding.radioButtonOption3.setChecked(true);
                            break;
                        case "d":
                            binding.radioButtonOption4.setChecked(true);
                            break;
                        case "repeat":
                            disableRadioButtons();
                            setQuestionList(mcqPosition);
                            break;
                        case "next":
                            if (mcqPosition == mcqList.size() - 1) {
                                textToSpeech.speak("No more MCQ available", TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
                            } else {
                                disableRadioButtons();
                                setQuestionList(mcqPosition = mcqPosition + 1);
                                isSpeakerOn = false;
                                binding.buttonTextToSpeech.setImageResource(R.drawable.ic_speaker_off);
                            }
                            break;
                        default:
                            textToSpeech.speak("Wrong Input. Please say between A, B, C, D or Next", TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
                            break;
                    }
                } else {
                    textToSpeech.speak("No Input. Please say between A, B, C, D, Repeat or Next", TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                Log.v("aa", "onPartialResults");
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                Log.v("aa", "onEvent");
            }
        });
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickSpeaker(View view) {
        if (!isSpeakerOn) {
            if (PermissionUtility.checkRecordAudioPermission(this)) {
                isSpeakerOn = true;
                binding.buttonTextToSpeech.setImageResource(R.drawable.ic_speaker_on);
                textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
            }
        } else {
            isSpeakerOn = false;
            binding.buttonTextToSpeech.setImageResource(R.drawable.ic_speaker_off);
            textToSpeech.stop();
        }
    }

    public void onClickPrevious(View view) {
        if (mcqPosition == 0) {
            AppUtilities.showToast(this, "No previous MCQ available");
        } else {
            disableRadioButtons();
            setQuestionList(mcqPosition = mcqPosition - 1);
        }
    }

    public void onClickNext(View view) {
        if (mcqPosition == mcqList.size() - 1) {
            AppUtilities.showToast(this, "No more MCQ available");
        } else {
            disableRadioButtons();
            setQuestionList(mcqPosition = mcqPosition + 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtility.RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isSpeakerOn = true;
                textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
            }
        }
    }

    private void setQuestionList(int mcqPosition) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        }, 300);
        enableRadioButtons();
        toSpeak = "";

        // Question
        if (mcqList.get(mcqPosition).question.txt != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).question.txt)) {
            binding.textViewQuestion.setText("Q. " + mcqList.get(mcqPosition).question.txt);
            toSpeak = mcqList.get(mcqPosition).question.txt + "." + "Options are.";
        }
        if (mcqList.get(mcqPosition).question.img != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).question.img))
            loadImage(binding.imageViewQuestion, mcqList.get(mcqPosition).question.img);
        else
            binding.imageViewQuestion.setVisibility(View.GONE);

        // Option A
        toSpeak += "A.";
        if (mcqList.get(mcqPosition).options.get(0).txt != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(0).txt)) {
            binding.radioButtonOption1.setText(mcqList.get(mcqPosition).options.get(0).txt);
            toSpeak += mcqList.get(mcqPosition).options.get(0).txt + ".";
        }
        if (mcqList.get(mcqPosition).options.get(0).img != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(0).img)) {
            loadImage(binding.imageViewOption1, mcqList.get(mcqPosition).options.get(0).img);
            toSpeak += "Image.";
        } else {
            binding.imageViewOption1.setVisibility(View.GONE);
        }

        // Option B
        toSpeak += "B.";
        if (mcqList.get(mcqPosition).options.get(1).txt != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(1).txt)) {
            binding.radioButtonOption2.setText(mcqList.get(mcqPosition).options.get(1).txt);
            toSpeak += mcqList.get(mcqPosition).options.get(1).txt + ".";
        }
        if (mcqList.get(mcqPosition).options.get(1).img != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(1).img)) {
            loadImage(binding.imageViewOption2, mcqList.get(mcqPosition).options.get(1).img);
            toSpeak += "Image.";
        } else {
            binding.imageViewOption2.setVisibility(View.GONE);
        }

        // Option C
        toSpeak += "C.";
        if (mcqList.get(mcqPosition).options.get(2).txt != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(2).txt)) {
            binding.radioButtonOption3.setText(mcqList.get(mcqPosition).options.get(2).txt);
            toSpeak += mcqList.get(mcqPosition).options.get(2).txt + ".";
        }
        if (mcqList.get(mcqPosition).options.get(2).img != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(2).img)) {
            loadImage(binding.imageViewOption3, mcqList.get(mcqPosition).options.get(2).img);
            toSpeak += "Image.";
        } else {
            binding.imageViewOption3.setVisibility(View.GONE);
        }

        // Option D
        toSpeak += "D.";
        if (mcqList.get(mcqPosition).options.get(3).txt != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(3).txt)) {
            binding.radioButtonOption4.setText(mcqList.get(mcqPosition).options.get(3).txt);
            toSpeak += mcqList.get(mcqPosition).options.get(3).txt + ".";
        }
        if (mcqList.get(mcqPosition).options.get(3).img != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(3).img)) {
            loadImage(binding.imageViewOption4, mcqList.get(mcqPosition).options.get(3).img);
            toSpeak += "Image.";
        } else {
            binding.imageViewOption4.setVisibility(View.GONE);
        }

        if (isSpeakerOn) {
            textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        }
    }

    private void loadImage(ImageView imageView, String imagePath) {
        imageView.setVisibility(View.VISIBLE);
        AppUtilities.loadImage(this, imageView, imagePath);
    }

    private void enableRadioButtons() {
        binding.radioGroupAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                clearSelection(false);
                View radioButton = binding.radioGroupAnswer.findViewById(checkedId);
                int index = binding.radioGroupAnswer.indexOfChild(radioButton);
                switch (index) {
                    case 0:
                        showAnswerStatus(index, mcqList.get(mcqPosition).correctOption == 1);
                        break;

                    case 2:
                        showAnswerStatus(index, mcqList.get(mcqPosition).correctOption == 2);
                        break;

                    case 4:
                        showAnswerStatus(index, mcqList.get(mcqPosition).correctOption == 3);
                        break;

                    case 6:
                        showAnswerStatus(index, mcqList.get(mcqPosition).correctOption == 4);
                        break;
                }
            }
        });
    }

    private void disableRadioButtons() {
        binding.radioGroupAnswer.setOnCheckedChangeListener(null);
        binding.radioGroupAnswer.clearCheck();
        clearSelection(true);
    }

    private void clearSelection(boolean isNewQuestion) {
        for (int i = 0; i < binding.radioGroupAnswer.getChildCount(); i++) {
            View view = binding.radioGroupAnswer.getChildAt(i);
            if (view instanceof RadioButton) {
                ((RadioButton) view).setButtonTintList(ColorStateList.valueOf(Color.GRAY));
                ((RadioButton) view).setTextColor(Color.BLACK);
            } else if (view instanceof ImageView) {
                ((ImageView) view).setBackgroundColor(Color.WHITE);
            }

            if (isNewQuestion) {
                if (view instanceof TextView) {
                    ((TextView) view).setText("");
                }
                if (view instanceof ImageView) {
                    ((ImageView) view).setVisibility(View.GONE);
                }
            }
        }

        binding.cardViewAnswer.setVisibility(View.GONE);
    }

    private void showAnswerStatus(int position, boolean isRight) {
        if (isRight) { // Right Answer
            ((RadioButton) binding.radioGroupAnswer.getChildAt(position)).setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorGreen)));
            ((RadioButton) binding.radioGroupAnswer.getChildAt(position)).setTextColor(getColor(R.color.colorGreen));
            ((ImageView) binding.radioGroupAnswer.getChildAt(position + 1)).setBackgroundColor(getColor(R.color.colorGreen));
        } else { // Wrong Answer
            ((RadioButton) binding.radioGroupAnswer.getChildAt(position)).setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorRed)));
            ((RadioButton) binding.radioGroupAnswer.getChildAt(position)).setTextColor(getColor(R.color.colorRed));
            ((ImageView) binding.radioGroupAnswer.getChildAt(position + 1)).setBackgroundColor(getColor(R.color.colorRed));

            // Show Correct Answer
            binding.cardViewAnswer.setVisibility(View.VISIBLE);
            if (mcqList.get(mcqPosition).options.get(mcqList.get(mcqPosition).correctOption - 1).txt != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(mcqList.get(mcqPosition).correctOption - 1).txt)) {
                binding.textViewAnswer.setText(mcqList.get(mcqPosition).options.get(mcqList.get(mcqPosition).correctOption - 1).txt);
                binding.textViewAnswer.setVisibility(View.VISIBLE);
            } else
                binding.textViewAnswer.setVisibility(View.GONE);

            if (mcqList.get(mcqPosition).options.get(mcqList.get(mcqPosition).correctOption - 1).img != null && !TextUtils.isEmpty(mcqList.get(mcqPosition).options.get(mcqList.get(mcqPosition).correctOption - 1).img))
                loadImage(binding.imageViewAnswer, mcqList.get(mcqPosition).options.get(mcqList.get(mcqPosition).correctOption - 1).img);
            else
                binding.imageViewAnswer.setVisibility(View.GONE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.scrollView.smoothScrollTo(0, (int) binding.cardViewAnswer.getY());
                }
            }, 300);
        }
    }

    private void getQuestionList() {
        if (AppUtilities.getInstance(this).isOnline()) {
            mProgressDialog.showProgressDialog();

            MCQListRequest mcqListRequest = new MCQListRequest();
            mcqListRequest.bookId = mBookDetails.bookId;
            mcqListRequest.category = mSessionManager.getSession(MCQ_CATEGORY);
            mcqListRequest.chapter = mSessionManager.getSession(CHAPTER);

            if (!mSessionManager.getBooleanSession(IS_SUBSCRIBED)) {
                retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).getPreviewMCQList(mcqListRequest),
                        UrlConstants.URL_PREVIEW_MCQ_LIST);
            } else {
                retroClient.makeHttpRequest(retroClient.retrofit.create(RetroClient.RestInterface.class).getMCQList(mcqListRequest),
                        UrlConstants.URL_MCQ_LIST);
            }
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_connection));
        }
    }

    @Override
    public void onSuccess(Call call, Response response, String method_name) {
        mProgressDialog.hideProgressDialog();
        MCQListResponse mcqListResponse = (MCQListResponse) response.body();
        if (mcqListResponse != null && mcqListResponse.retCode && mcqListResponse.returnData.size() > 0) {
            mcqList = (ArrayList<ReturnData>) mcqListResponse.returnData;
            if (mcqList.size() > 0) {
                setQuestionList(mcqPosition);
            } else {
                showSnackBar(binding.rootLayout, getString(R.string.no_mcq));
            }
        } else {
            showSnackBar(binding.rootLayout, getString(R.string.no_mcq));
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        mProgressDialog.hideProgressDialog();
        showSnackBar(binding.rootLayout, getString(R.string.something_went_wrong));
    }
}