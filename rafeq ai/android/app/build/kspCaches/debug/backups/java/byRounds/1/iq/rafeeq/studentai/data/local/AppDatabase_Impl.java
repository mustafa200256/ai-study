package iq.rafeeq.studentai.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import iq.rafeeq.studentai.data.local.dao.ChatDao;
import iq.rafeeq.studentai.data.local.dao.ChatDao_Impl;
import iq.rafeeq.studentai.data.local.dao.NoteDao;
import iq.rafeeq.studentai.data.local.dao.NoteDao_Impl;
import iq.rafeeq.studentai.data.local.dao.PaymentDao;
import iq.rafeeq.studentai.data.local.dao.PaymentDao_Impl;
import iq.rafeeq.studentai.data.local.dao.QuizDao;
import iq.rafeeq.studentai.data.local.dao.QuizDao_Impl;
import iq.rafeeq.studentai.data.local.dao.TaskDao;
import iq.rafeeq.studentai.data.local.dao.TaskDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile TaskDao _taskDao;

  private volatile NoteDao _noteDao;

  private volatile QuizDao _quizDao;

  private volatile ChatDao _chatDao;

  private volatile PaymentDao _paymentDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `study_tasks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `subject` TEXT NOT NULL, `dueDate` TEXT NOT NULL, `dueTime` TEXT NOT NULL, `priority` TEXT NOT NULL, `isCompleted` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `notes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `content` TEXT NOT NULL, `subject` TEXT NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `quiz_questions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `subjectId` TEXT NOT NULL, `subjectName` TEXT NOT NULL, `question` TEXT NOT NULL, `optionA` TEXT NOT NULL, `optionB` TEXT NOT NULL, `optionC` TEXT NOT NULL, `optionD` TEXT NOT NULL, `correctIndex` INTEGER NOT NULL, `explanation` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `quiz_attempts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `subjectName` TEXT NOT NULL, `totalQuestions` INTEGER NOT NULL, `correctAnswers` INTEGER NOT NULL, `wrongAnswers` INTEGER NOT NULL, `scorePercentage` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `chat_messages` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `isUser` INTEGER NOT NULL, `content` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `isError` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `payment_requests` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `planName` TEXT NOT NULL, `priceIqd` TEXT NOT NULL, `walletType` TEXT NOT NULL, `senderPhone` TEXT NOT NULL, `transactionRef` TEXT NOT NULL, `status` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4064527a326bbb953aeeab8cb3649e4a')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `study_tasks`");
        db.execSQL("DROP TABLE IF EXISTS `notes`");
        db.execSQL("DROP TABLE IF EXISTS `quiz_questions`");
        db.execSQL("DROP TABLE IF EXISTS `quiz_attempts`");
        db.execSQL("DROP TABLE IF EXISTS `chat_messages`");
        db.execSQL("DROP TABLE IF EXISTS `payment_requests`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsStudyTasks = new HashMap<String, TableInfo.Column>(8);
        _columnsStudyTasks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyTasks.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyTasks.put("subject", new TableInfo.Column("subject", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyTasks.put("dueDate", new TableInfo.Column("dueDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyTasks.put("dueTime", new TableInfo.Column("dueTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyTasks.put("priority", new TableInfo.Column("priority", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyTasks.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyTasks.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStudyTasks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStudyTasks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStudyTasks = new TableInfo("study_tasks", _columnsStudyTasks, _foreignKeysStudyTasks, _indicesStudyTasks);
        final TableInfo _existingStudyTasks = TableInfo.read(db, "study_tasks");
        if (!_infoStudyTasks.equals(_existingStudyTasks)) {
          return new RoomOpenHelper.ValidationResult(false, "study_tasks(iq.rafeeq.studentai.data.local.entity.StudyTaskEntity).\n"
                  + " Expected:\n" + _infoStudyTasks + "\n"
                  + " Found:\n" + _existingStudyTasks);
        }
        final HashMap<String, TableInfo.Column> _columnsNotes = new HashMap<String, TableInfo.Column>(5);
        _columnsNotes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("subject", new TableInfo.Column("subject", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNotes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNotes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNotes = new TableInfo("notes", _columnsNotes, _foreignKeysNotes, _indicesNotes);
        final TableInfo _existingNotes = TableInfo.read(db, "notes");
        if (!_infoNotes.equals(_existingNotes)) {
          return new RoomOpenHelper.ValidationResult(false, "notes(iq.rafeeq.studentai.data.local.entity.NoteEntity).\n"
                  + " Expected:\n" + _infoNotes + "\n"
                  + " Found:\n" + _existingNotes);
        }
        final HashMap<String, TableInfo.Column> _columnsQuizQuestions = new HashMap<String, TableInfo.Column>(10);
        _columnsQuizQuestions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("subjectId", new TableInfo.Column("subjectId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("subjectName", new TableInfo.Column("subjectName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("question", new TableInfo.Column("question", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("optionA", new TableInfo.Column("optionA", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("optionB", new TableInfo.Column("optionB", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("optionC", new TableInfo.Column("optionC", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("optionD", new TableInfo.Column("optionD", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("correctIndex", new TableInfo.Column("correctIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizQuestions.put("explanation", new TableInfo.Column("explanation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysQuizQuestions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesQuizQuestions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoQuizQuestions = new TableInfo("quiz_questions", _columnsQuizQuestions, _foreignKeysQuizQuestions, _indicesQuizQuestions);
        final TableInfo _existingQuizQuestions = TableInfo.read(db, "quiz_questions");
        if (!_infoQuizQuestions.equals(_existingQuizQuestions)) {
          return new RoomOpenHelper.ValidationResult(false, "quiz_questions(iq.rafeeq.studentai.data.local.entity.QuizQuestionEntity).\n"
                  + " Expected:\n" + _infoQuizQuestions + "\n"
                  + " Found:\n" + _existingQuizQuestions);
        }
        final HashMap<String, TableInfo.Column> _columnsQuizAttempts = new HashMap<String, TableInfo.Column>(7);
        _columnsQuizAttempts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("subjectName", new TableInfo.Column("subjectName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("totalQuestions", new TableInfo.Column("totalQuestions", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("correctAnswers", new TableInfo.Column("correctAnswers", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("wrongAnswers", new TableInfo.Column("wrongAnswers", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("scorePercentage", new TableInfo.Column("scorePercentage", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysQuizAttempts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesQuizAttempts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoQuizAttempts = new TableInfo("quiz_attempts", _columnsQuizAttempts, _foreignKeysQuizAttempts, _indicesQuizAttempts);
        final TableInfo _existingQuizAttempts = TableInfo.read(db, "quiz_attempts");
        if (!_infoQuizAttempts.equals(_existingQuizAttempts)) {
          return new RoomOpenHelper.ValidationResult(false, "quiz_attempts(iq.rafeeq.studentai.data.local.entity.QuizAttemptEntity).\n"
                  + " Expected:\n" + _infoQuizAttempts + "\n"
                  + " Found:\n" + _existingQuizAttempts);
        }
        final HashMap<String, TableInfo.Column> _columnsChatMessages = new HashMap<String, TableInfo.Column>(5);
        _columnsChatMessages.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("isUser", new TableInfo.Column("isUser", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatMessages.put("isError", new TableInfo.Column("isError", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChatMessages = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesChatMessages = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoChatMessages = new TableInfo("chat_messages", _columnsChatMessages, _foreignKeysChatMessages, _indicesChatMessages);
        final TableInfo _existingChatMessages = TableInfo.read(db, "chat_messages");
        if (!_infoChatMessages.equals(_existingChatMessages)) {
          return new RoomOpenHelper.ValidationResult(false, "chat_messages(iq.rafeeq.studentai.data.local.entity.ChatMessageEntity).\n"
                  + " Expected:\n" + _infoChatMessages + "\n"
                  + " Found:\n" + _existingChatMessages);
        }
        final HashMap<String, TableInfo.Column> _columnsPaymentRequests = new HashMap<String, TableInfo.Column>(8);
        _columnsPaymentRequests.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaymentRequests.put("planName", new TableInfo.Column("planName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaymentRequests.put("priceIqd", new TableInfo.Column("priceIqd", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaymentRequests.put("walletType", new TableInfo.Column("walletType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaymentRequests.put("senderPhone", new TableInfo.Column("senderPhone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaymentRequests.put("transactionRef", new TableInfo.Column("transactionRef", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaymentRequests.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaymentRequests.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPaymentRequests = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPaymentRequests = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPaymentRequests = new TableInfo("payment_requests", _columnsPaymentRequests, _foreignKeysPaymentRequests, _indicesPaymentRequests);
        final TableInfo _existingPaymentRequests = TableInfo.read(db, "payment_requests");
        if (!_infoPaymentRequests.equals(_existingPaymentRequests)) {
          return new RoomOpenHelper.ValidationResult(false, "payment_requests(iq.rafeeq.studentai.data.local.entity.PaymentRequestEntity).\n"
                  + " Expected:\n" + _infoPaymentRequests + "\n"
                  + " Found:\n" + _existingPaymentRequests);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "4064527a326bbb953aeeab8cb3649e4a", "295391684160a9b3d5deff86a5bb6481");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "study_tasks","notes","quiz_questions","quiz_attempts","chat_messages","payment_requests");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `study_tasks`");
      _db.execSQL("DELETE FROM `notes`");
      _db.execSQL("DELETE FROM `quiz_questions`");
      _db.execSQL("DELETE FROM `quiz_attempts`");
      _db.execSQL("DELETE FROM `chat_messages`");
      _db.execSQL("DELETE FROM `payment_requests`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TaskDao.class, TaskDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(NoteDao.class, NoteDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(QuizDao.class, QuizDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChatDao.class, ChatDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PaymentDao.class, PaymentDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public TaskDao taskDao() {
    if (_taskDao != null) {
      return _taskDao;
    } else {
      synchronized(this) {
        if(_taskDao == null) {
          _taskDao = new TaskDao_Impl(this);
        }
        return _taskDao;
      }
    }
  }

  @Override
  public NoteDao noteDao() {
    if (_noteDao != null) {
      return _noteDao;
    } else {
      synchronized(this) {
        if(_noteDao == null) {
          _noteDao = new NoteDao_Impl(this);
        }
        return _noteDao;
      }
    }
  }

  @Override
  public QuizDao quizDao() {
    if (_quizDao != null) {
      return _quizDao;
    } else {
      synchronized(this) {
        if(_quizDao == null) {
          _quizDao = new QuizDao_Impl(this);
        }
        return _quizDao;
      }
    }
  }

  @Override
  public ChatDao chatDao() {
    if (_chatDao != null) {
      return _chatDao;
    } else {
      synchronized(this) {
        if(_chatDao == null) {
          _chatDao = new ChatDao_Impl(this);
        }
        return _chatDao;
      }
    }
  }

  @Override
  public PaymentDao paymentDao() {
    if (_paymentDao != null) {
      return _paymentDao;
    } else {
      synchronized(this) {
        if(_paymentDao == null) {
          _paymentDao = new PaymentDao_Impl(this);
        }
        return _paymentDao;
      }
    }
  }
}
