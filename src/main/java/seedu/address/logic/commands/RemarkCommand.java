package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_JOBS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.job.Job;
import seedu.address.model.job.Remark;

/**
 * Changes the remark of an existing person in the address book.
 */
public class RemarkCommand extends Command {

    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the remark of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing remark will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "r/ [REMARK]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "r/ Likes to swim.";

    public static final String MESSAGE_ADD_REMARK_SUCCESS = "Added remark to Job: ";
    public static final String MESSAGE_DELETE_REMARK_SUCCESS = "Removed remark from Job: ";

    private final Index index;
    private final Remark remark;

    /**
     * @param index of the person in the filtered person list to edit the remark
     * @param remark of the person to be updated to
     */
    public RemarkCommand(Index index, Remark remark) {
        requireAllNonNull(index, remark);

        this.index = index;
        this.remark = remark;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Job> lastShownList = model.getFilteredJobList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
        }

        Job jobToEdit = lastShownList.get(index.getZeroBased());
        Job editedJob = new Job(
                jobToEdit.getRole(), jobToEdit.getCompany(), remark);

        model.setJob(jobToEdit, editedJob);
        model.updateFilteredJobList(PREDICATE_SHOW_ALL_JOBS);

        return new CommandResult(generateSuccessMessage(editedJob));
    }

    /**
     * Generates a command execution success message based on whether
     * the remark is added to or removed from
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Job jobToEdit) {
        String jobTitle = String.valueOf(jobToEdit.getCompany());
        String message = !remark.value.isEmpty()
                ? MESSAGE_ADD_REMARK_SUCCESS + jobTitle
                : MESSAGE_DELETE_REMARK_SUCCESS + jobTitle;
        return String.format(message, jobToEdit);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RemarkCommand)) {
            return false;
        }

        RemarkCommand e = (RemarkCommand) other;
        return index.equals(e.index)
                && remark.equals(e.remark);
    }
}
