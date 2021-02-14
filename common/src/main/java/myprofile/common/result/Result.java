package myprofile.common.result;

import myprofile.common.error.MyProfileError;

import java.util.ArrayList;
import java.util.List;

public class Result<T>{
    private T ok;
    private boolean validated = false;
    private List<MyProfileError> errorCodes = new ArrayList<>();

    public Result(T ok) {
        this.ok = ok;
    }

    public Result(MyProfileError error) {
        this.errorCodes.add(error);
    }

    public Result(List<MyProfileError> errores) {
        this.errorCodes.addAll(errores);
    }

    public T ok() throws RuntimeException {
        if (!validated || isError()) {
            throw new RuntimeException("OK no existe. Error");
        }
        return ok;
    }

    public Result addError(MyProfileError error) {
        this.errorCodes.add(error);
        return this;
    }

    public boolean isError() {
        validated = true;
        return this.errorCodes.size() > 0;
    }

    public static <T> Result<T> ok(T ok) {
        return new Result<T>(ok);
    }

    public static <T> Result<T> ok(Result ok) {
        if (ok == null) {
            return new Result<T>((T) null);
        } else {
            return new Result<T>((T) ok.ok());
        }
    }

    public static Result error(Result error) {
        return error(error.getErrorCodes());
    }

    public static <T> Result<T> error(String message) {
        return error(new MyProfileError(message));
    }

    public static Result error(MyProfileError error) {
        return new Result(error);
    }

    public static Result error(List<MyProfileError> errores) {
        return new Result(errores);
    }

    public List<MyProfileError> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<MyProfileError> errorCodes) {
        this.errorCodes = errorCodes;
    }

    @Override
    public String toString() {
        return "Result{" +
                "ok=" + ok +
                ", validated=" + validated +
                ", errorsCodes=" + errorCodes +
                '}';
    }

    public T getOk() {
        return ok;
    }
}


