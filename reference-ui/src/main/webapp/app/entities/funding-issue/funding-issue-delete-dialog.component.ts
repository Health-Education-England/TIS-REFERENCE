import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {FundingIssue} from "./funding-issue.model";
import {FundingIssuePopupService} from "./funding-issue-popup.service";
import {FundingIssueService} from "./funding-issue.service";

@Component({
	selector: 'jhi-funding-issue-delete-dialog',
	templateUrl: './funding-issue-delete-dialog.component.html'
})
export class FundingIssueDeleteDialogComponent {

	fundingIssue: FundingIssue;

	constructor(private jhiLanguageService: JhiLanguageService,
				private fundingIssueService: FundingIssueService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['fundingIssue']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.fundingIssueService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'fundingIssueListModification',
				content: 'Deleted an fundingIssue'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-funding-issue-delete-popup',
	template: ''
})
export class FundingIssueDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private fundingIssuePopupService: FundingIssuePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.fundingIssuePopupService
				.open(FundingIssueDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
