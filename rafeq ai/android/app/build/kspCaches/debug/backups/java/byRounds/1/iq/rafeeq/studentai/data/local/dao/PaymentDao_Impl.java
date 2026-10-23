package iq.rafeeq.studentai.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import iq.rafeeq.studentai.data.local.entity.PaymentRequestEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class PaymentDao_Impl implements PaymentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PaymentRequestEntity> __insertionAdapterOfPaymentRequestEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;

  public PaymentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPaymentRequestEntity = new EntityInsertionAdapter<PaymentRequestEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `payment_requests` (`id`,`planName`,`priceIqd`,`walletType`,`senderPhone`,`transactionRef`,`status`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PaymentRequestEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getPlanName());
        statement.bindString(3, entity.getPriceIqd());
        statement.bindString(4, entity.getWalletType());
        statement.bindString(5, entity.getSenderPhone());
        statement.bindString(6, entity.getTransactionRef());
        statement.bindString(7, entity.getStatus());
        statement.bindLong(8, entity.getCreatedAt());
      }
    };
    this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE payment_requests SET status = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertPayment(final PaymentRequestEntity req,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPaymentRequestEntity.insertAndReturnId(req);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStatus(final long id, final String status,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, status);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PaymentRequestEntity>> getAllPayments() {
    final String _sql = "SELECT * FROM payment_requests ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"payment_requests"}, new Callable<List<PaymentRequestEntity>>() {
      @Override
      @NonNull
      public List<PaymentRequestEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlanName = CursorUtil.getColumnIndexOrThrow(_cursor, "planName");
          final int _cursorIndexOfPriceIqd = CursorUtil.getColumnIndexOrThrow(_cursor, "priceIqd");
          final int _cursorIndexOfWalletType = CursorUtil.getColumnIndexOrThrow(_cursor, "walletType");
          final int _cursorIndexOfSenderPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "senderPhone");
          final int _cursorIndexOfTransactionRef = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionRef");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<PaymentRequestEntity> _result = new ArrayList<PaymentRequestEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PaymentRequestEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPlanName;
            _tmpPlanName = _cursor.getString(_cursorIndexOfPlanName);
            final String _tmpPriceIqd;
            _tmpPriceIqd = _cursor.getString(_cursorIndexOfPriceIqd);
            final String _tmpWalletType;
            _tmpWalletType = _cursor.getString(_cursorIndexOfWalletType);
            final String _tmpSenderPhone;
            _tmpSenderPhone = _cursor.getString(_cursorIndexOfSenderPhone);
            final String _tmpTransactionRef;
            _tmpTransactionRef = _cursor.getString(_cursorIndexOfTransactionRef);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new PaymentRequestEntity(_tmpId,_tmpPlanName,_tmpPriceIqd,_tmpWalletType,_tmpSenderPhone,_tmpTransactionRef,_tmpStatus,_tmpCreatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
