package lotto.domain.model;

import lotto.domain.util.ErrorMessage;
import lotto.view.OutputView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LottoWinnerSystem {

    private final int WINNER_NUMBER_COUNT = 6;
    private final int BONUS_NUMBER_COUNT = 1;

    private List<Integer> winNumbers;
    private List<Integer> bonusNumber;

    private static int first;
    private static int second;
    private static int third;
    private static int fourth;
    private static int fifth;

    public LottoWinnerSystem() {
        this.first = 0;
        this.second = 0;
        this.third = 0;
        this.fourth = 0;
        this.fifth = 0;
    }

    public static int calculateProfitMoney() {
        int profitMoney = 5000 * fifth + 50000 * fourth + 1500000 * third + 30000000 * second + 2000000000 * first;
        return profitMoney;
    }

    public void printLottoResult() {
        OutputView.printTotalResult(fifth, fourth, third, second, first);
    }

    // 발행한 로또가 당첨인지 확인
    public void isWinnerLotto(List<Lotto> purchasedLotto, List<Integer> winNumber, List<Integer> bonusNumber) {
        for (int i = 0; i < purchasedLotto.size(); i++) {
            List<Integer> compareLotto = purchasedLotto.get(i).getLotto();
            compareOneLotto(winNumber, bonusNumber, compareLotto);
        }
    }

    private void compareOneLotto(List<Integer> winNumber, List<Integer> bonusNumber, List<Integer> compareLotto) {
        int matchCount = getMatchNumberCount(compareLotto, winNumber);
        if (matchCount >= 3) {
            if (matchCount == 3) {
                fifth += 1;
                return;
            }
            if (matchCount == 4) {
                fourth += 1;
                return;
            }
            if (matchCount == 5) {
                if (isContainBonusNumber(compareLotto, bonusNumber)) {
                    second += 1;
                    return;
                }
                third += 1;
                return;
            }
            first += 1;
        }
    }

    // 보너스 번호만 검사 (5개 일치할 때만 실행)
    public boolean isContainBonusNumber(List<Integer> lottoNumber, List<Integer> bonusNumber) {
        if (lottoNumber.contains(bonusNumber.get(0))) {
            return true;
        }
        return false;
    }

    // 보너스 번호를 제외하여 비교
    public int getMatchNumberCount(List<Integer> lottoNumber, List<Integer> winNumber) {
        int matchCount = 0;
        for (int i = 0; i < winNumber.size(); i++) {
            if (lottoNumber.contains(winNumber.get(i))) {
                matchCount += 1;
            }
        }
        return matchCount;
    }

    public List<Integer> isValidWinNumber(String input) {
        try {
            List<Integer> numbers = convertIntegerList(input);
            isValidLength(numbers, WINNER_NUMBER_COUNT);
            isDistinct(numbers);
            isValidRange(numbers);

            this.winNumbers = numbers;
            return numbers;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_TYPE_INPUT.getErrorMessage());
        }
    }

    public List<Integer> isValidBonusNumber(String input, List<Integer> winNumbers) {
        try {
            List<Integer> numbers = convertIntegerList(input);
            isValidLength(numbers, BONUS_NUMBER_COUNT);
            isValidRange(numbers);
            isContainWinNumbers(winNumbers, numbers);

            this.bonusNumber = numbers;
            return numbers;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_TYPE_INPUT.getErrorMessage());
        }
    }

    private List<Integer> convertIntegerList(String input) {
        List<Integer> numbers = Stream.of(input.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        return numbers;
    }

    private static void isValidLength(List<Integer> numbers, int numberCount) {
        if (numbers.size() != numberCount) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_LENGTH_INPUT.getErrorMessage());
        }
    }

    public static void isDistinct(List<Integer> numbers) {
        Set<Integer> distinctNumbers = new HashSet<>(numbers);
        if (distinctNumbers.size() != numbers.size()) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATE_NUMBER.getErrorMessage());
        }
    }

    private static void isValidRange(List<Integer> numbers) {
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i) < 1 || numbers.get(i) > 45) {
                throw new IllegalArgumentException(ErrorMessage.INVALID_RANGE_NUMBER.getErrorMessage());
            }
        }
    }

    private void isContainWinNumbers(List<Integer> winNumbers, List<Integer> numbers) {
        if (winNumbers.contains(numbers.get(0))) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATE_BONUS_NUMBER.getErrorMessage());
        }
    }
}
