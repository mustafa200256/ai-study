package iq.rafeeq.studentai.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import iq.rafeeq.studentai.data.local.entity.QuizAttemptEntity;
import iq.rafeeq.studentai.data.local.entity.QuizQuestionEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class QuizDao_Impl implements QuizDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<QuizQuestionEntity> __insertionAdapterOfQuizQuestionEntity;

  private final EntityInsertionAdapter<QuizAttemptEntity> __insertionAdapterOfQuizAttemptEntity;

  public QuizDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfQuizQuestionEntity = new EntityInsertionAdapter<QuizQuestionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `quiz_questions` (`id`,`subjectId`,`subjectName`,`question`,`optionA`,`optionB`,`optionC`,`optionD`,`correctIndex`,`explanation`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final QuizQuestionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getSubjectId());
        statement.bindString(3, entity.getSubjectName());
        statement.bindString(4, entity.getQuestion());
        statement.bindString(5, entity.getOptionA());
        statement.bindString(6, entity.getOptionB());
        statement.bindString(7, entity.getOptionC());
        statement.bindString(8, entity.getOptionD());
        statement.bindLong(9, entity.getCorrectIndex());
        statement.bindString(10, entity.getExplanation());
      }
    };
    this.__insertionAdapterOfQuizAttemptEntity = new EntityInsertionAdapter<QuizAttemptEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `quiz_attempts` (`id`,`subjectName`,`totalQuestions`,`correctAnswers`,`wrongAnswers`,`scorePercentage`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final QuizAttemptEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getSubjectName());
        statement.bindLong(3, entity.getTotalQuestions());
        statement.bindLong(4, entity.getCorrectAnswers());
        statement.bindLong(5, entity.getWrongAnswers());
        statement.bindLong(6, entity.getScorePercentage());
        statement.bindLong(7, entity.getTimestamp());
      }
    };
  }

  @Override
  public Object insertQuestions(final List<QuizQuestionEntity> questions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfQuizQuestionEntity.insert(questions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAttempt(final QuizAttemptEntity attempt,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfQuizAttemptEntity.insertAndReturnId(attempt);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getQuestionsBySubject(final String subjectId,
      final Continuation<? super List<QuizQuestionEntity>> $completion) {
    final String _sql = "SELECT * FROM quiz_questions WHERE subjectId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, subjectId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<QuizQuestionEntity>>() {
      @Override
      @NonNull
      public List<QuizQuestionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfSubjectName = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectName");
          final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
          final int _cursorIndexOfOptionA = CursorUtil.getColumnIndexOrThrow(_cursor, "optionA");
          final int _cursorIndexOfOptionB = CursorUtil.getColumnIndexOrThrow(_cursor, "optionB");
          final int _cursorIndexOfOptionC = CursorUtil.getColumnIndexOrThrow(_cursor, "optionC");
          final int _cursorIndexOfOptionD = CursorUtil.getColumnIndexOrThrow(_cursor, "optionD");
          final int _cursorIndexOfCorrectIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "correctIndex");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final List<QuizQuestionEntity> _result = new ArrayList<QuizQuestionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QuizQuestionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSubjectId;
            _tmpSubjectId = _cursor.getString(_cursorIndexOfSubjectId);
            final String _tmpSubjectName;
            _tmpSubjectName = _cursor.getString(_cursorIndexOfSubjectName);
            final String _tmpQuestion;
            _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
            final String _tmpOptionA;
            _tmpOptionA = _cursor.getString(_cursorIndexOfOptionA);
            final String _tmpOptionB;
            _tmpOptionB = _cursor.getString(_cursorIndexOfOptionB);
            final String _tmpOptionC;
            _tmpOptionC = _cursor.getString(_cursorIndexOfOptionC);
            final String _tmpOptionD;
            _tmpOptionD = _cursor.getString(_cursorIndexOfOptionD);
            final int _tmpCorrectIndex;
            _tmpCorrectIndex = _cursor.getInt(_cursorIndexOfCorrectIndex);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            _item = new QuizQuestionEntity(_tmpId,_tmpSubjectId,_tmpSubjectName,_tmpQuestion,_tmpOptionA,_tmpOptionB,_tmpOptionC,_tmpOptionD,_tmpCorrectIndex,_tmpExplanation);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getQuestionsCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM quiz_questions";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<QuizAttemptEntity>> getAllAttempts() {
    final String _sql = "SELECT * FROM quiz_attempts ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"quiz_attempts"}, new Callable<List<QuizAttemptEntity>>() {
      @Override
      @NonNull
      public List<QuizAttemptEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectName = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectName");
          final int _cursorIndexOfTotalQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuestions");
          final int _cursorIndexOfCorrectAnswers = CursorUtil.getColumnIndexOrThrow(_cursor, "correctAnswers");
          final int _cursorIndexOfWrongAnswers = CursorUtil.getColumnIndexOrThrow(_cursor, "wrongAnswers");
          final int _cursorIndexOfScorePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "scorePercentage");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<QuizAttemptEntity> _result = new ArrayList<QuizAttemptEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QuizAttemptEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSubjectName;
            _tmpSubjectName = _cursor.getString(_cursorIndexOfSubjectName);
            final int _tmpTotalQuestions;
            _tmpTotalQuestions = _cursor.getInt(_cursorIndexOfTotalQuestions);
            final int _tmpCorrectAnswers;
            _tmpCorrectAnswers = _cursor.getInt(_cursorIndexOfCorrectAnswers);
            final int _tmpWrongAnswers;
            _tmpWrongAnswers = _cursor.getInt(_cursorIndexOfWrongAnswers);
            final int _tmpScorePercentage;
            _tmpScorePercentage = _cursor.getInt(_cursorIndexOfScorePercentage);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new QuizAttemptEntity(_tmpId,_tmpSubjectName,_tmpTotalQuestions,_tmpCorrectAnswers,_tmpWrongAnswers,_tmpScorePercentage,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getTotalAttemptsCount() {
    final String _sql = "SELECT COUNT(*) FROM quiz_attempts";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"quiz_attempts"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Double> getAverageScore() {
    final String _sql = "SELECT AVG(scorePercentage) FROM quiz_attempts";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"quiz_attempts"}, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
