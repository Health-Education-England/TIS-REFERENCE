import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {FundingIssue} from "./funding-issue.model";
import {FundingIssuePopupService} from "./funding-issue-popup.service";
import {FundingIssueService} from "./funding-issue.service";
@Component({
	selector: 'jhi-funding-issue-dialog',
	templateUrl: './funding-issue-dialog.component.html'
})
export class FundingIssueDialogComponent implements OnInit {

	fundingIssue: FundingIssue;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private fundingIssueService: FundingIssueService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['fundingIssue']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.fundingIssue.id !== undefined) {
			this.fundingIssueService.update(this.fundingIssue)
				.subscribe((res: FundingIssue) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.fundingIssueService.create(this.fundingIssue)
				.subscribe((res: FundingIssue) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: FundingIssue) {
		this.eventManager.broadcast({name: 'fundingIssueListModification', content: 'OK'});
		this.isSaving = false;
		this.activeModal.dismiss(result);
	}

	private onSaveError(error) {
		this.isSaving = false;
		this.onError(error);
	}

	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}

@Component({
	selector: 'jhi-funding-issue-popup',
	template: ''
})
export class FundingIssuePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private fundingIssuePopupService: FundingIssuePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.fundingIssuePopupService
					.open(FundingIssueDialogComponent, params['id']);
			} else {
				this.modalRef = this.fundingIssuePopupService
					.open(FundingIssueDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
