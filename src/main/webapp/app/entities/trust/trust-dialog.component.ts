import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {Trust} from "./trust.model";
import {TrustPopupService} from "./trust-popup.service";
import {TrustService} from "./trust.service";
import {LocalOffice, LocalOfficeService} from "../local-office";
@Component({
	selector: 'jhi-trust-dialog',
	templateUrl: './trust-dialog.component.html'
})
export class TrustDialogComponent implements OnInit {

	trust: Trust;
	authorities: any[];
	isSaving: boolean;

	localoffices: LocalOffice[];

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private trustService: TrustService,
				private localOfficeService: LocalOfficeService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['trust']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
		this.localOfficeService.query().subscribe(
			(res: Response) => {
				this.localoffices = res.json();
			}, (res: Response) => this.onError(res.json()));
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.trust.id !== undefined) {
			this.trustService.update(this.trust)
				.subscribe((res: Trust) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.trustService.create(this.trust)
				.subscribe((res: Trust) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: Trust) {
		this.eventManager.broadcast({name: 'trustListModification', content: 'OK'});
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

	trackLocalOfficeById(index: number, item: LocalOffice) {
		return item.id;
	}
}

@Component({
	selector: 'jhi-trust-popup',
	template: ''
})
export class TrustPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private trustPopupService: TrustPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.trustPopupService
					.open(TrustDialogComponent, params['id']);
			} else {
				this.modalRef = this.trustPopupService
					.open(TrustDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
