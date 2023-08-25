package com.example.cbc_main.Detail

import EventDecorator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.cbc_main.MyApplication
import com.example.cbc_main.RidingDatabase
import com.example.cbc_main.data.ShockRequest
import com.example.cbc_main.data.ShockResponse
import com.example.cbc_main.databinding.FragmentDetail1Binding
import com.example.cbc_main.retrofit.RetrofitClass
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Response
import java.util.*


class Detail1Fragment : Fragment(), OnDateSelectedListener {

    //context
    lateinit var detailActivity: DetailActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
         detailActivity = context as DetailActivity
    }

    //binding
    private var _binding: FragmentDetail1Binding?=null
    private val binding get()=_binding!!

    //keyboard
    var imm: InputMethodManager? = null

    //memo
    var str: String = ""
    private var Formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetail1Binding.inflate(inflater, container, false)

        detailActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


        //calendar
        binding.calendarview.state()
            .edit()
            .setFirstDayOfWeek(DayOfWeek.of(Calendar.SUNDAY))
            .commit()

        //room
        val db = Room.databaseBuilder(
            detailActivity, AppDatabase::class.java,
            "todo-db"
        ).allowMainThreadQueries().build()

        val Rdb = Room.databaseBuilder(
            detailActivity, RidingDatabase::class.java,
            "riding-db"
        ).allowMainThreadQueries().build()

        //오늘 날짜로
        var calendar: LocalDate = LocalDate.now()
        binding.date.text = Formatter.format(calendar)
        binding.calendarview.setSelectedDate(calendar)

        //충격 값 받는 레트로핏
        getShock()

        //메모 있을 때
        if (db.MemoDao().getMemo(binding.date.text.toString()) != null) {
            binding.TextView.text = db.MemoDao().getMemo(binding.date.text.toString())
            showTextView()
        } else { //해당 날짜에 메모가 없을 때
            binding.TextView.text = db.MemoDao().getMemo(binding.date.text.toString())
            showTextView()
        }

        //라이딩 기록
        if(Rdb.RidingDao().getTime(binding.date.text.toString()) != null) {
            binding.tvspeed.text =
                "속도\n" + Rdb.RidingDao().getSpeed(binding.date.text.toString())
            binding.tvtime.text =
                "시간\n" + Rdb.RidingDao().getTime(binding.date.text.toString())
            binding.tvdistance.text =
                "거리\n" + Rdb.RidingDao().getDistance(binding.date.text.toString())
            binding.tvkcal.text =
                "칼로리\n" + Rdb.RidingDao().getCalorie(binding.date.text.toString())
        }


        UrlDate.urldate = binding.date.text.toString()

        //다른 날짜 선택시
        binding.calendarview.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
            if (selected) {
                //날짜 띄우기
                binding.date.text = (Formatter.format(date.date))
                UrlDate.urldate = binding.date.text.toString()
                //충격 값
                getShock()

                //라이딩 기록
                if(Rdb.RidingDao().getTime(binding.date.text.toString()) != null) {
                    binding.tvspeed.text =
                        "속도\n" + Rdb.RidingDao().getSpeed(binding.date.text.toString())
                    binding.tvtime.text =
                        "시간\n" + Rdb.RidingDao().getTime(binding.date.text.toString())
                    binding.tvdistance.text =
                        "거리\n" + Rdb.RidingDao().getDistance(binding.date.text.toString())
                    binding.tvkcal.text =
                        "칼로리\n" + Rdb.RidingDao().getCalorie(binding.date.text.toString())
                }
                //해당 날짜에 메모가 있을 때
                if (db.MemoDao().getMemo(binding.date.text.toString()) != null) {
                    binding.TextView.text = db.MemoDao().getMemo(binding.date.text.toString())
                    showTextView()
                } else { //해당 날짜에 메모가 없을 때
                    binding.TextView.text = db.MemoDao().getMemo(binding.date.text.toString())
                    showTextView()
                }
            } else {
                binding.date.text = Formatter.format(calendar)
            }
        })

        //키보드 매니저
        imm = detailActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        //TextView 누르면 editText로 이동
        binding.TextView.setOnClickListener {
                showEditTextView()
                binding.editText.post(Runnable {
                    kotlin.run {
                        binding.editText.isFocusableInTouchMode = true
                        binding.editText.requestFocus()
                        //show keyboard
                        imm?.showSoftInput(binding.editText, 0)
                    }
                })
                if (db.MemoDao().getMemo(binding.date.text.toString()) == null) {
                    binding.editText.setText("")
                } else {
                    str = binding.TextView.text.toString()
                    binding.editText.setText(str)
                    binding.editText.setSelection(binding.editText.length())
                }
        }

        //save 버튼
        binding.save.setOnClickListener {
                //hide keyboard
                var view = activity?.currentFocus
                if(view==null) {view =View(activity)}
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
                if (db.MemoDao().getMemo(binding.date.text.toString()) == null && binding.editText.isVisible) {
                    db.MemoDao()
                        .insert(Memo(binding.date.text.toString(), binding.editText.text.toString()))
                } else if (db.MemoDao().getMemo(binding.date.text.toString()) != null && binding.editText.isVisible) {
                    db.MemoDao()
                        .updateMemo(binding.editText.text.toString(), binding.date.text.toString())
                }
                binding.editText.setText("")
                showTextView()
                binding.TextView.text = db.MemoDao().getMemo(binding.date.text.toString())

                onResume()
            }

        //삭제 버튼
        binding.delete.setOnClickListener {
            //db에서 삭제
            db.MemoDao().deleteMemo(binding.date.text.toString())

            showTextView()
            binding.TextView.text = db.MemoDao().getMemo(binding.date.text.toString())

            binding.calendarview.removeDecorators()

            onResume()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val db = Room.databaseBuilder(
            detailActivity, AppDatabase::class.java,
            "todo-db"
        ).allowMainThreadQueries().build()

        val Rdb = Room.databaseBuilder(
            detailActivity, RidingDatabase::class.java,
            "riding-db"
        ).allowMainThreadQueries().build()

        val eventlist: List<String> = db.MemoDao().getDate()
        Log.d("Memo", eventlist.toString())
        for (i in eventlist.indices){
            val eventDate = eventlist[i].split("-")
            val year = Integer.parseInt(eventDate[0])
            val month = Integer.parseInt(eventDate[1])
            val day = Integer.parseInt(eventDate[2])
            Log.d("Memo", year.toString() + month.toString() + day.toString())
            binding.calendarview
                .addDecorator(
                    EventDecorator(
                        Color.parseColor("#3D5220"),
                        Collections.singleton(CalendarDay.from(year, month, day))
                    ))
        }

        val eventlist2: List<String> = Rdb.RidingDao().getDate()
        for (i in eventlist2.indices){
            val eventDate = eventlist2[i].split("-")
            val year = Integer.parseInt(eventDate[0])
            val month = Integer.parseInt(eventDate[1])
            val day = Integer.parseInt(eventDate[2])
            Log.d("Memo", year.toString() + month.toString() + day.toString())
            binding.calendarview
                .addDecorator(
                    EventDecorator(
                        Color.parseColor("#3D5220"),
                        Collections.singleton(CalendarDay.from(year, month, day))
                    ))
        }
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
    }

    //TextView 보이게
    fun showEditTextView(){
        binding.editText.visibility = View.VISIBLE
        binding.TextView.visibility = View.INVISIBLE
    }

    fun showTextView(){
        binding.editText.visibility = View.INVISIBLE
        binding.TextView.visibility = View.VISIBLE
    }

    fun getShock(){
        //충격 값 받는 레트로핏
        val service = RetrofitClass.shockService

        val shockRequest = ShockRequest(binding.date.text.toString())

        val apikey = MyApplication.prefs.getString("token", "")
        // 실제 반복하는 코드를 여기에 적는다
        Log.d("token", apikey)

        val callGetS = service.postShock(apikey,shockRequest)

        callGetS.enqueue(object : retrofit2.Callback<ShockResponse>{
            override fun onResponse(call: Call<ShockResponse>, response: Response<ShockResponse>) {
                val shock: String
                shock = response.body()?.MESSAGE.toString()
                binding.tvshock.setText("라이딩 기록\n\n오늘 라이딩 동안 충격 횟수는 "+shock+"회 입니다.")
                Log.d("Shock", response.body()?.MESSAGE.toString())
            }

            override fun onFailure(call: Call<ShockResponse>, t: Throwable) {
                Log.e("Shock", t.message.toString())
            }
        })
    }
}