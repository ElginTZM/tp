package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSETUTORIAL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIALNUMBER;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.predicate.AbsentFromTutorialPredicate;
import seedu.address.model.predicate.ContainsTagPredicate;
import seedu.address.model.tag.Tag;

/**
 * Lists all persons in the address book to the user.
 */
public class ListAttendanceCommand extends ListCommand {

    public static final String COMMAND_WORD = "attendance";
    public static final String MESSAGE_USAGE = "list " + COMMAND_WORD
            + ": Lists summary of attendance and absent students.\n"
            + "Parameters: "
            + PREFIX_COURSETUTORIAL + "TAG "
            + PREFIX_TUTORIALNUMBER + "TUTORIALNUMBER (must be a positive integer)\n"
            + "Example: list " + COMMAND_WORD + " coursetg/CS2103 " + "tn/1";
    public static final String MESSAGE_SUCCESS = "Listed all absent students";

    private final Index tn;
    private final AbsentFromTutorialPredicate absencePredicate;
    private final Tag tag;
    private final ContainsTagPredicate courseTutorialPredicate;

    /**
     * @param tag Tutorial group to list
     * @param tn Tutorial number to list
     * @param courseTutorialPredicate Predicate used to filter for students in the tutorial group
     * @param absencePredicate Predicate used to filter for students absent
     */
    public ListAttendanceCommand(Tag tag, Index tn,
                                 ContainsTagPredicate courseTutorialPredicate,
                                 AbsentFromTutorialPredicate absencePredicate) {
        requireNonNull(tn);
        this.tn = tn;
        this.absencePredicate = absencePredicate;
        this.tag = tag;
        this.courseTutorialPredicate = courseTutorialPredicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        Tag placeholder = new Tag("PLACEHOLDER");
        String tutorialStr = String.format(" for Tutorial #%d", tn.getOneBased());

        if (!tag.equals(placeholder)) {
            model.addFilter(courseTutorialPredicate);
            String tutorialGroupStr = String.format(" from %s.", tag.getTagName());
            tutorialStr += tutorialGroupStr;
        }

        int numberOfStudents = model.getFilteredPersonList().size();

        model.addFilter(absencePredicate);

        int numberOfAbsentees = model.getFilteredPersonList().size();
        int numberOfPresentees = numberOfStudents - numberOfAbsentees;

        String attendanceNumberStr = String.format("%d of %d students present.\n",
                numberOfPresentees, numberOfStudents);

        return new CommandResult(attendanceNumberStr + MESSAGE_SUCCESS + tutorialStr);
    }
}
