import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {LocalOfficeDetailComponent} from "../../../../../../main/webapp/app/entities/local-office/local-office-detail.component";
import {LocalOfficeService} from "../../../../../../main/webapp/app/entities/local-office/local-office.service";
import {LocalOffice} from "../../../../../../main/webapp/app/entities/local-office/local-office.model";

describe('Component Tests', () => {

	describe('LocalOffice Management Detail Component', () => {
		let comp: LocalOfficeDetailComponent;
		let fixture: ComponentFixture<LocalOfficeDetailComponent>;
		let service: LocalOfficeService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [LocalOfficeDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					LocalOfficeService
				]
			}).overrideComponent(LocalOfficeDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(LocalOfficeDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(LocalOfficeService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new LocalOffice(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.localOffice).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
